import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import { updateUser } from 'redux/actions/user';

import Input from 'reacts/pages/components/common/Input';
import SelectTransparent from 'reacts/pages/components/common/SelectTransparent';

import * as s from 'reacts/styles/components/user/EditSettings';
const EditSettings = ({ toggle, setToggle }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const groundsMine = useSelector((state) => state.user.groundsMine);
  const [selected, setSelected] = useState(user ? user.groundsMine[0] : {});
  const [selectedName, setSelectedName] = useState(user ? user.groundsMine[0].name : '');
  const [groundName, setGroundName] = useState(user.groundsMine[0].name);
  const [focusTime, setFocusTime] = useState(user.groundsMine[0].focusTime);
  const [activeTime, setActiveTime] = useState(user.groundsMine[0].activeTime);

  useEffect(() => {
    if (toggle) {
      eetch
        .userGrounds({ accessToken: user.accessToken, refreshToken: user.refreshToken })
        .then((grounds) => {
          const groundsMap = grounds.data.filter((ground) => ground.isOwner === true).map((ground) => ground.ground);
          groundsMine.current = groundsMap;
          dispatch(
            updateUser({
              groundsMine: groundsMap,
            }),
          );
        })
        .catch((err) => {
          if (err.message === 'RefreshTokenExpired') {
            dispatch(logoutUser());
            dispatch(setMenu(false));
            dispatch(setMessage(false));
            navigate(`/login`);
          }
        });
    }
  }, [toggle]);

  const submitChange = () => {
    eetch
      .editGround({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: selected.id,
        name: groundName,
        focusTime,
        activeTime,
      })
      .then(() => {
        setSelectedName(groundName);
        eetch
          .userGrounds({ accessToken: user.accessToken, refreshToken: user.refreshToken })
          .then((grounds) => {
            const groundsMap = grounds.data.filter((ground) => ground.isOwner === true).map((ground) => ground.ground);
            groundsMine.current = groundsMap;
            dispatch(
              updateUser({
                groundsMine: groundsMap,
              }),
            );
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

  useEffect(() => {
    setGroundName(selected.name);
    setFocusTime(selected.focusTime);
    setActiveTime(selected.activeTime);
  }, [selected]);

  useEffect(() => {
    eetch.groundUsers({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: selected.id }).then((res) => {
      console.log(res);
    });

    eetch
      .groundUser({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: selected.id, email: 'gayun0303@gmail.com' })
      .then((res) => {
        console.log(res);
        if (res.data.length > 0)
          eetch
            .groundInvite({
              accessToken: user.accessToken,
              refreshToken: user.refreshToken,
              groundId: selected.id,
              githubId: res.data[0].github_id,
            })
            .then((res) => console.log(res));
      });

    eetch.groundOut({ accessToken: user.accessToken, refreshToken: user.refreshToken, groundId: selected.id, githubId: 111165249 }).then((res) => {
      console.log(res);
    });
  }, []);

  return (
    <>
      <s.OutWrapper $toggle={toggle} onClick={() => setToggle(false)} />
      <s.EditWrapper $toggle={toggle} onClick={() => setToggle(false)}>
        <s.EditModalWrapper onClick={(event) => event.stopPropagation()}>
          {groundsMine.length > 0 ? (
            <>
              <SelectTransparent label="그라운드" list={groundsMine} select={setSelected} selected={selectedName} />
              <Input label="그라운드 명" data={groundName} setData={setGroundName} refPing={selected} />
              <Input label="집중시간" data={focusTime} setData={setFocusTime} />
              <Input label="활동시간" data={activeTime} setData={setActiveTime} />
              <s.ButtonWrapper>
                <s.ProfileEditButton type="button" onClick={submitChange}>
                  적용
                </s.ProfileEditButton>
                <s.CloseButton onClick={() => setToggle(false)}>닫기</s.CloseButton>
              </s.ButtonWrapper>
            </>
          ) : (
            <s.ButtonWrapper>
              <s.CloseButton onClick={() => setToggle(false)}>닫기</s.CloseButton>
            </s.ButtonWrapper>
          )}
        </s.EditModalWrapper>
        <p onClick={submitChange}>hello</p>
      </s.EditWrapper>
    </>
  );
};

export default EditSettings;
