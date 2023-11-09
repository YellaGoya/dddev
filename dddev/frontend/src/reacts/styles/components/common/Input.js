import styled from 'styled-components';

export const InputWrapper = styled.div`
  position: relative;
  margin-bottom: 1.5rem;
`;

export const Label = styled.div`
  position: absolute;
  left: 40px;
  top: -9px;
  padding: 0 5px;
  color: var(--font-border);
  background-color: var(--bg-dark);

  font-weight: 500;
  font-size: 0.8rem;
`;

export const Input = styled.input`
  width: calc(100% - 50px);
  height: 55px;

  border-radius: 0.75rem;
  border: 1px solid ${({ $isActive }) => ($isActive ? 'var(--border-focus)' : 'var(--border-basic)')};
  background-color: transparent;

  font-size: 0.9rem;

  margin: 0 25px;
  color: var(--font-rev);
  padding: ${({ $isClick }) => ($isClick ? '0 57px 0 20px' : '0 20px 0 20px')};

  &:focus {
    outline: none;
  }
`;

export const DivLine = styled.div`
  position: absolute;
  top: 3px;
  right: 41px;
  width: 1px;
  height: 23px;

  background-color: var(--border-basic);
`;
export const Button = styled.button`
  position: absolute;
  right: 28px;
  top: 50%;
  transform: translateY(calc(-50% + 1px));
  background-color: transparent;
  border: none;
  display: block;
  width: fit-content;

  cursor: pointer;

  & > svg {
    fill: var(--font-border);
  }
`;

export const Message = styled.p`
  position: absolute;
  right: 35px;
  bottom: -32px;
  font-size: 0.8rem;
  color: ${({ $fail }) => ($fail ? 'var(--font-warning)' : 'var(--font-border)')};
`;

export const ArrayList = styled.ul`
  list-style: none;
  padding: 0;
  margin: 0;

  & > li {
    display: inline-block;
  }
`;

export const ArrayFixed = styled.li`
  cursor: default;
`;

export const ArrayItem = styled.li`
  margin: 0 4px;
  padding: 0;

  cursor: pointer;
`;
