import { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import Input from 'reacts/pages/components/common/Input';

import * as s from 'reacts/styles/components/user/EditSettings';
const EditSettings = ({ toggle, setToggle, groundInfo }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const [groundName, setGroundName] = useState(groundInfo.name);
  const [focusTime, setFocusTime] = useState(groundInfo.focusTime);
  const [activeTime, setActiveTime] = useState(groundInfo.activeTime);

  useEffect(() => {
    setGroundName(groundInfo.name);
    setFocusTime(groundInfo.focusTime);
    setActiveTime(groundInfo.activeTime);
  }, [groundInfo]);

  const submitChange = () => {
    eetch
      .editGround({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: user.lastGround,
        name: groundName,
        focusTime,
        activeTime,
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

  return (
    <>
      <s.OutWrapper $toggle={toggle} onClick={() => setToggle(false)} />
      <s.EditWrapper $toggle={toggle}>
        <s.EditModalWrapper>
          <Input label="그라운드 명" data={groundName} setData={setGroundName} />
          <Input label="집중시간" data={focusTime} setData={setFocusTime} />
          <Input label="활동시간" data={activeTime} setData={setActiveTime} />
          <s.ButtonWrapper>
            <s.ProfileEditButton type="button" onClick={submitChange}>
              적용
            </s.ProfileEditButton>
            <s.CloseButton onClick={() => setToggle(false)}>닫기</s.CloseButton>
          </s.ButtonWrapper>
        </s.EditModalWrapper>
        <p onClick={submitChange}>hello</p>
      </s.EditWrapper>
    </>
  );
};

export default EditSettings;
