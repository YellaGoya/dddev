import { useState, useEffect, useRef } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { useNavigate } from 'react-router-dom';

import eetch from 'eetch/eetch';
import { setMenu } from 'redux/actions/menu';
import { setMessage } from 'redux/actions/menu';
import { logoutUser } from 'redux/actions/user';

import Input from 'reacts/pages/components/common/Input';
import SelectTransparent from 'reacts/pages/components/common/SelectTransparent';

import * as s from 'reacts/styles/components/user/EditSettings';
const EditSettings = ({ toggle, setToggle, groundInfo }) => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const user = useSelector((state) => state.user);
  const groundsMap = useSelector((state) => state.user.groundsMap);
  const groundNameRef = useRef(groundInfo ? groundInfo.name : '');
  const focusTimeRef = useRef(groundInfo ? groundInfo.focusTime : '');
  const activeTimeRef = useRef(groundInfo ? groundInfo.activeTime : '');
  const [selected, setSelected] = useState(groundInfo);

  useEffect(() => {
    if (groundInfo) {
      groundNameRef.current = groundInfo.name;
      focusTimeRef.current = groundInfo.focusTime;
      activeTimeRef.current = groundInfo.activeTime;
    }
  }, [groundInfo]);

  useEffect(() => {
    console.log(selected);
    groundNameRef.current = selected.name;
    focusTimeRef.current = selected.focusTime;
    activeTimeRef.current = selected.activeTime;
  }, [selected]);

  const submitChange = () => {
    eetch
      .editGround({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: user.lastGround,
        name: groundNameRef.current,
        focusTime: focusTimeRef.current,
        activeTime: activeTimeRef.current,
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
      <s.EditWrapper $toggle={toggle} onClick={() => setToggle(false)}>
        <s.EditModalWrapper onClick={(event) => event.stopPropagation()}>
          <SelectTransparent label="그라운드" list={groundsMap} selected={groundNameRef.current} select={setSelected} />
          <Input label="그라운드 명" dataRef={groundNameRef} />
          <Input label="집중시간" dataRef={focusTimeRef} />
          <Input label="활동시간" dataRef={activeTimeRef} />
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
