import * as s from 'markup/styles/components/common/Input';

const Input = ({ label, data, setData, enter, click, message }) => {
  const changeHandler = (e) => {
    setData(e.target.value);
  };

  // input 태그 enter키 핸들러
  const keyDownHandler = (e) => {
    if (e.key === 'Enter') {
      enter();
    }
  };

  return (
    <s.InputWrapper>
      {label && <s.Label>{label}</s.Label>}
      <s.Input value={data} onChange={changeHandler} onKeyDown={keyDownHandler} />
      {click && (
        <s.Button type="button" onClick={click}>
          확인
        </s.Button>
      )}
      {message && <s.Message>{message}</s.Message>}
    </s.InputWrapper>
  );
};

export default Input;
