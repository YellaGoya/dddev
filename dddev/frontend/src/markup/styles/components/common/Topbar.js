import styled from 'styled-components';

export const TopbarWrapper = styled.header`
  position: relative;
  width: 100%;
  height: 35px;

  display: flex;
  align-items: center;

  background-color: beige;

  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
`;

export const MenuButton = styled.button`
  margin: auto 0;
`;

export const SelectedGround = styled.h1`
  display: inline-block;

  font-size: 1.3rem;
  cursor: pointer;
`;
