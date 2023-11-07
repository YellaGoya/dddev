import { useState, useEffect } from 'react';
import ExitToAppRoundedIcon from '@mui/icons-material/ExitToAppRounded';

import * as s from 'reacts/styles/components/common/Input';
const Input = ({ label, data = '', holder, setData, array, fixed, click, enter, message, debounce }) => {
  const [value, setValue] = useState(data === null ? '' : data);
  const [isData, setIsData] = useState(true); // [TODO] [REFACTORING
  const [placeholder, setPlaceholder] = useState(holder || '');
  const [isActive, setIsActive] = useState(false);
  const [timer, setTimer] = useState(null);

  const onChangeHandler = (e) => {
    if (debounce) clearTimeout(timer);
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
    return array.filter((item) => item !== i);
  };

  useEffect(() => {
    if (isData && data) {
      setValue(data);
      setIsData(false);
    }
  }, [data, isData]);

  useEffect(() => {
    if (debounce && value !== '') {
      setTimer(
        setTimeout(() => {
          debounce();
        }, 2000),
      );
    }
  }, [value]);

  const handleFocus = () => {
    setIsActive(true);
  };

  const handleBlur = () => {
    setIsActive(false);
  };

  return (
    <s.InputWrapper>
      {label && <s.Label $isActive={isActive}>{label}</s.Label>}
      <s.Input
        value={value}
        placeholder={placeholder}
        $isActive={isActive}
        $isClick={Boolean(click)}
        onChange={onChangeHandler}
        onKeyDown={keyDownHandler}
        onFocus={handleFocus}
        onBlur={handleBlur}
      />
      {click && (
        <s.Button type="button" onClick={() => click(value)}>
          <s.DivLine />
          <ExitToAppRoundedIcon />
        </s.Button>
      )}
      {message && <s.Message $fail={message.fail}>{message.text}</s.Message>}
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
