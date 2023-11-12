import { useState, useRef, useEffect } from 'react';

import userStockImage from 'assets/userStockImage.webp';
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';

import * as s from 'reacts/styles/components/common/SelectUser';
const SelectUser = ({ label, list, select, selected, keyInsert }) => {
  const [toggle, setToggle] = useState(false);
  const [value, setValue] = useState({});
  const selRef = useRef();

  const selectHandler = (item) => {
    if (select) select(item);
    setValue(item);
    setToggle(false);
  };

  useEffect(() => {
    setValue(selected);
  }, [selected]);

  document.addEventListener('click', (e) => {
    if (e.target !== selRef.current) setToggle(false);
  });

  return (
    <s.SelectWrapper>
      <s.Selected ref={selRef} $toggle={toggle} onClick={() => setToggle(!toggle)}>
        {label && <s.Label>{label}</s.Label>}
        <p>{value ? value.nickname : '지정 전'}</p>
        {value && (
          <s.SelectedProfileImage src={value.profileDto ? `https://k9d103.p.ssafy.io/img/user/${value.profileDto.fileName}` : userStockImage} />
        )}
        <KeyboardArrowDownIcon />
      </s.Selected>
      {list && list.length > 0 && (
        <s.SelectList $toggle={toggle}>
          {list.map((item) => (
            <s.SelectItem key={keyInsert + item.userId} onClick={() => selectHandler(item)}>
              <s.ProfileImage src={item.profileDto ? `https://k9d103.p.ssafy.io/img/user/${item.profileDto.fileName}` : userStockImage} />
              <p>{item.nickname}</p>
            </s.SelectItem>
          ))}
        </s.SelectList>
      )}
    </s.SelectWrapper>
  );
};

export default SelectUser;
