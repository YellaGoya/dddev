import { useState, useRef } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import eetch from 'eetch/eetch';
import Input from 'reacts/pages/components/common/Input';

import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import userStockImage from 'assets/userStockImage.webp';
import DisabledByDefaultRoundedIcon from '@mui/icons-material/DisabledByDefaultRounded';
import PhotoFilterIcon from '@mui/icons-material/PhotoFilter';

import * as s from 'reacts/styles/components/user/EditProfile';
const EditProfile = ({ toggle, setToggle, userInfo, setUserInfo }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const [pat, setPat] = useState(userInfo.personalAccessToken && userInfo.personalAccessToken.substr(0, 3).concat(' ···'));
  const [nickname, setNickname] = useState(userInfo.nickname);
  const [statusMsg, setStatusMsg] = useState(userInfo.statusMsg);
  const [patMessage, setPatMessage] = useState(null);
  const hiddenFileInput = useRef(null);

  const handleFileSelect = () => {
    hiddenFileInput.current.click();
  };

  const handleChange = (event) => {
    const formData = new FormData();
    formData.append('file', event.target.files[0]);

    eetch
      .userUploadImage({ accessToken: user.accessToken, refreshToken: user.refreshToken, formData })
      .then(() => {
        eetch
          .userInfo({ accessToken: user.accessToken, refreshToken: user.refreshToken })
          .then((res) => {
            setUserInfo(res.data);
          })
          .catch((err) => {
            if (err.message === 'RefreshTokenExpired') {
              dispatch(logoutUser());
              dispatch(setMenu(false));
              dispatch(setMessage(false));
              navigate(`/login`);
            }
          });
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

  const submitPat = () => {
    eetch
      .githubTokenRegist({ accessToken: user.accessToken, refreshToken: user.refreshToken, personalAccessToken: pat })
      .then(() => {
        setPatMessage('통과');
      })
      .catch(() => {
        setPatMessage('틀려먹음');
      });
  };

  const submitChange = () => {
    eetch.userEdit({ accessToken: user.accessToken, refreshToken: user.refreshToken, nickname, statusMsg }).catch((err) => {
      console.log(err);
    });
  };

  const deleteImage = () => {
    eetch.userDeleteImage({ accessToken: user.accessToken, refreshToken: user.refreshToken }).catch((err) => {
      console.log(err);
    });
  };

  return (
    <>
      <s.OutWrapper $toggle={toggle} onClick={() => setToggle(false)} />
      <s.EditWrapper $toggle={toggle}>
        <s.EditModalWrapper>
          유저 프로필 수정이 들어갈 곳
          <s.EditImageWrapper>
            <s.ProfileImage src={userInfo.profileDto ? `https://k9d103.p.ssafy.io/img/user/${userInfo.profileDto.fileName}` : userStockImage} />
            <s.ImageButton onClick={handleFileSelect}>
              <PhotoFilterIcon />
            </s.ImageButton>
            <s.ImageButton onClick={deleteImage}>
              <DisabledByDefaultRoundedIcon />
            </s.ImageButton>
            <input ref={hiddenFileInput} type="file" style={{ display: 'none' }} onChange={handleChange} />
          </s.EditImageWrapper>
          <Input label="Pat" holder={pat} setData={setPat} enter={submitPat} click={submitPat} message={patMessage} />
          <Input label="닉네임" data={nickname} setData={setNickname} />
          <Input label="상태 메시지" data={statusMsg} setData={setStatusMsg} />
          <s.ProfileEditButton type="button" onClick={submitChange}>
            변경
          </s.ProfileEditButton>
          <p onClick={() => setToggle(false)}>close</p>
        </s.EditModalWrapper>
      </s.EditWrapper>
    </>
  );
};

export default EditProfile;
