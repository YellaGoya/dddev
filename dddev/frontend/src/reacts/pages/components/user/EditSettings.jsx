import { useState, useEffect } from 'react';
import { useSelector } from 'react-redux';
import eetch from 'eetch/eetch';

import Input from 'reacts/pages/components/common/Input';

import * as s from 'reacts/styles/components/user/EditSettings';
const EditSettings = ({ toggle, setToggle, groundInfo }) => {
  const user = useSelector((state) => state.user);
  const [groundName, setGroundName] = useState(groundInfo.name);
  const [focusTime, setFocusTime] = useState(groundInfo.focusTime);
  const [activeTime, setActiveTime] = useState(groundInfo.activeTime);

  useEffect(() => {
    setGroundName(groundInfo.name);
    setFocusTime(groundInfo.focusTime);
    setActiveTime(groundInfo.activeTime);
  }, [groundInfo]);

  const submitHandler = () => {
    eetch
      .editGround({
        accessToken: user.accessToken,
        refreshToken: user.refreshToken,
        groundId: user.lastGround,
        name: groundName,
        focusTime,
        activeTime,
      })
      .then((res) => {
        console.log(res);
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
          <p onClick={() => setToggle(false)}>close</p>
        </s.EditModalWrapper>
        <p onClick={submitHandler}>hello</p>
      </s.EditWrapper>
    </>
  );
};

export default EditSettings;
