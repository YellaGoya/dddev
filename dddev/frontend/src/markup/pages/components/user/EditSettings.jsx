import { useState, useEffect } from 'react';
import Input from 'markup/pages/components/common/Input';

import * as s from 'markup/styles/components/user/EditSettings';
const EditSettings = ({ toggle, setToggle, groundInfo }) => {
  const [groundName, setGroundName] = useState(groundInfo.name);
  const [focustTime, setFocustTime] = useState(groundInfo.focusTime);
  const [activeTime, setActiveTime] = useState(groundInfo.activeTime);

  useEffect(() => {
    setGroundName(groundInfo.name);
    setFocustTime(groundInfo.focusTime);
    setActiveTime(groundInfo.activeTime);
  }, [groundInfo]);

  return (
    <>
      <s.OutWrapper $toggle={toggle} onClick={() => setToggle(false)} />
      <s.EditWrapper $toggle={toggle}>
        <s.EditModalWrapper>
          <Input label="그라운드 명" data={groundName} setData={setGroundName} />
          <Input label="집중시간" data={focustTime} setData={setFocustTime} />
          <Input label="활동시간" data={activeTime} setData={setActiveTime} />
          <p onClick={() => setToggle(false)}>close</p>
        </s.EditModalWrapper>
      </s.EditWrapper>
    </>
  );
};

export default EditSettings;
