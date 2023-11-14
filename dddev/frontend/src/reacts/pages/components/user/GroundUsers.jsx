import { useEffect, useState } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import eetch from 'eetch/eetch';
import userStockImage from 'assets/userStockImage.webp';
import Input from 'reacts/pages/components/common/Input';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import AutoAwesomeRoundedIcon from '@mui/icons-material/AutoAwesomeRounded';
import PersonOffRoundedIcon from '@mui/icons-material/PersonOffRounded';
import * as s from 'reacts/styles/components/user/GroundUsers';

const GroundUsers = ({ selected }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const [isInvite, setIsInvite] = useState(false);
  const [email, setEmail] = useState('');
  const [resultId, setResultId] = useState(null);
  const [users, setUsers] = useState(null);

  const refreshMemberList = () => {
    if (selected)
      eetch
        .groundUsers({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: selected.id })
        .then((res) => {
          setUsers(res.data);
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
  };

  const checkEmail = (data, set) => {
    eetch
      .groundUser({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: selected.id, email: data })
      .then((res) => {
        if (res.data.length > 0) {
          set({ fail: false, text: `* ${res.data[0].github_name}님을 초대할까요?` });
          setResultId(res.data[0].github_id);
        } else set({ fail: true, text: '* 그라운드 밖에서 찾을 수 없습니다.' });
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
    setResultId(null);
  };

  const inviteUser = ({ setMessage, setHolder }) => {
    eetch
      .groundInvite({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: selected.id,
        githubId: resultId,
      })
      .then(() => {
        refreshMemberList();
        if (setHolder) setHolder(email);
        setEmail('');
        if (setMessage) setMessage({ fail: false, text: '* 멤버로 추가 하였습니다.' });
        setResultId('');
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  const removeUser = (githubId) => {
    eetch
      .groundOut({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: selected.id, githubId })
      .then(() => {
        refreshMemberList();
      })
      .catch((err) => {
        if (err.message === 'RefreshTokenExpired') {
          dispatch(logoutUser());
          dispatch(setMenu(false));
          dispatch(setMessage(false));
          navigate(`/login`);
        }
      });
  };

  useEffect(() => {
    refreshMemberList();
  }, [selected]);

  return (
    <s.UsersWrapper>
      <s.List>
        <s.Users>
          <s.User onClick={() => setIsInvite(true)}>멤버 추가</s.User>
          {users &&
            users.map((user) => (
              <s.User key={user.userId}>
                <s.ProfileImage src={user.profileDto ? `https://k9d103.p.ssafy.io/img/user/${user.profileDto.fileName}` : userStockImage} />
                {user.nickname}{' '}
                {user.isOwner ? (
                  <>
                    <AutoAwesomeRoundedIcon className="ownerIcon" />
                    <h3 className="ownerText">그라운드 소유</h3>
                  </>
                ) : (
                  <s.RemoveButton onClick={() => removeUser(user.githubId)}>
                    <PersonOffRoundedIcon />
                    멤버 제거
                  </s.RemoveButton>
                )}
              </s.User>
            ))}
        </s.Users>
      </s.List>

      <Input display={isInvite} label="추가 대상 이메일" data={email} setData={setEmail} valid={checkEmail} enter={inviteUser} click={inviteUser} />

      <s.Label>그라운드 멤버</s.Label>
    </s.UsersWrapper>
  );
};

export default GroundUsers;
