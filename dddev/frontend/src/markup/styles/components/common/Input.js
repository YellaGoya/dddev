import styled from 'styled-components';

export const InputWrapper = styled.div``;

export const Label = styled.div`
  color: #fff;
`;

export const Input = styled.input`
  width: calc(100% - 50px);

  margin: 0 25px;
`;

export const Button = styled.button``;

export const Message = styled.p`
  color: #ff5555;
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
