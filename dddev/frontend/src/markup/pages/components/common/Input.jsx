import { useState, useEffect } from 'react';

import * as s from 'markup/styles/components/common/Input';
const Input = ({ label, data = '', holder, setData, array, fixed, click, enter, message }) => {
  const [value, setValue] = useState(data === null ? '' : data);
  const [placeholder, setPlaceholder] = useState(holder || '');

  const onChangeHandelr = (e) => {
    setValue(e.target.value);
    if (setData) setData(e.target.value);
  };

  const keyDownHandler = (e) => {
    if (e.key === 'Enter') {
      if (array) {
        if (!array.includes(value)) enter([...array, value]);
        setPlaceholder(value);
        setValue('');
      } else if (enter) enter(value);
    }
  };

  const removeCategory = (i) => {
    console.log(array.length, array);
    return array.filter((item) => item !== i);
  };

  useEffect(() => {
    if (data) setValue(data);
  }, [data]);

  return (
    <s.InputWrapper>
      {label && <s.Label>{label}</s.Label>}
      <s.Input value={value} placeholder={placeholder} onChange={onChangeHandelr} onKeyDown={keyDownHandler} />
      {click && (
        <s.Button type="button" onClick={() => click(value)}>
          확인
        </s.Button>
      )}
      {message && <s.Message>{message}</s.Message>}
      {array ? (
        <s.ArrayList>
          {fixed && fixed.map((item) => <s.ArrayFixed key={item}>{item}</s.ArrayFixed>)}
          {array.map((item) => {
            return (
              <s.ArrayItem key={item} onClick={() => enter(removeCategory(item))}>
                {item}
              </s.ArrayItem>
            );
          })}
        </s.ArrayList>
      ) : null}
    </s.InputWrapper>
  );
};

export default Input;
