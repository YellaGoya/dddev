import { useState, useRef, useEffect } from 'react';

import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown';
import * as s from 'reacts/styles/components/common/SelectTransparent';

const Select = ({ label, list, select, selected }) => {
  const [toggle, setToggle] = useState(false);
  const [value, setValue] = useState(list ? list[0].name : '');
  const selRef = useRef();

  const selectHandler = (item) => {
    if (select) select(item);
    setValue(item.name);
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
      {label && <s.Label>{label}</s.Label>}
      <s.Selected ref={selRef} $toggle={toggle} onClick={() => setToggle(!toggle)}>
        <p>{value}</p>
        <KeyboardArrowDownIcon />
      </s.Selected>
      {list && list.length > 0 && (
        <s.SelectList $toggle={toggle}>
          {list.map((item) => (
            <s.SelectItem key={item.id} onClick={() => selectHandler(item)}>
              <p>{item.name}</p>
            </s.SelectItem>
          ))}
        </s.SelectList>
      )}
    </s.SelectWrapper>
  );
};

export default Select;
