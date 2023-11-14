import { useState } from 'react';

import ExitToAppRoundedIcon from '@mui/icons-material/ExitToAppRounded';
import * as s from 'reacts/styles/components/common/Input';
const Input = ({ label, holder, data, setData, array, fixed, click, enter, message, valid, display = true, type }) => {
  const [value, setValue] = useState('');
  const [placeholder, setPlaceholder] = useState(holder || '');
  const [isActive, setIsActive] = useState(false);
  const [timer, setTimer] = useState(null);
  const [validMessage, setValidMessage] = useState(null);

  const onChangeHandler = (e) => {
    if (array) setValue(e.target.value);
    else setData(e.target.value);

    if (valid) {
      setTimer(
        setTimeout(() => {
          valid(e.target.value, setValidMessage);
        }, 500),
      );
    }

    if (valid) clearTimeout(timer);
  };

  const keyDownHandler = (e) => {
    if (e.key === 'Enter') {
      if (array) {
        if (!array.includes(e.target.value)) enter([...array, e.target.value]);
        setPlaceholder(e.target.value);
        setValue('');
      } else if (enter) enter({ data: e.target.value, setMessage: setValidMessage, setHolder: setPlaceholder });
    }
  };

  const removeCategory = (i) => {
    return array.filter((item) => item !== i);
  };

  const handleFocus = () => {
    setIsActive(true);
  };

  const handleBlur = () => {
    setIsActive(false);
  };

  return (
    <s.InputWrapper $display={display}>
      {label && <s.Label $isActive={isActive}>{label}</s.Label>}
      <s.Input
        type={type}
        value={array ? value : data}
        placeholder={placeholder}
        $isActive={isActive}
        $isClick={Boolean(click)}
        onChange={onChangeHandler}
        onKeyDown={keyDownHandler}
        onFocus={handleFocus}
        onBlur={handleBlur}
      />
      {click && (
        <s.Button type="button" onClick={() => click(array ? value : { data, setMessage: setValidMessage, setHolder: setPlaceholder })}>
          <s.DivLine />
          <ExitToAppRoundedIcon />
        </s.Button>
      )}
      {validMessage && <s.Message $fail={validMessage.fail}>{validMessage.text}</s.Message>}
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
