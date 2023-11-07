import { useState, useEffect, useRef } from 'react';

import * as s from 'reacts/styles/components/common/Select';
const Select = ({ label, list, data, select }) => {
  const [toggle, setToggle] = useState(false);
  const [value, setValue] = useState('');
  const selRef = useRef();

  const selectHandler = (item) => {
    if (select) select(item);
    setValue(item.name);
    setToggle(false);
  };

  useEffect(() => {
    if (list && list.length > 0) setValue(data ? data : list[0].name);
  }, [data, list]);

  document.addEventListener('click', (e) => {
    if (e.target !== selRef.current) setToggle(false);
  });

  return (
    <s.SelectWrapper>
      {label && <s.Label>{label}</s.Label>}
      <s.Selected ref={selRef} onClick={() => setToggle(!toggle)}>
        {value}
      </s.Selected>
      {toggle && list && list.length > 0 && (
        <s.SelectList>
          {list.map((item) => (
            <s.SelectItem key={item.id} onClick={() => selectHandler(item)}>
              {item.name}
            </s.SelectItem>
          ))}
        </s.SelectList>
      )}
    </s.SelectWrapper>
  );
};

export default Select;
