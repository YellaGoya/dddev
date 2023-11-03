import React from 'react';

// import Input from 'markup/pages/components/common/Input';

import * as s from 'markup/styles/components/user/EditSettings';
const EditSettings = ({ toggle, setToggle }) => {
  return (
    <>
      <s.OutWrapper $toggle={toggle} onClick={() => setToggle(false)} />
      <s.EditWrapper $toggle={toggle}>
        <s.EditModalWrapper>
          설정 가즈아
          <p onClick={() => setToggle(false)}>close</p>
        </s.EditModalWrapper>
      </s.EditWrapper>
    </>
  );
};

export default EditSettings;
