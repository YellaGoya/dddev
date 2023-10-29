import styled from 'styled-components';

export const TopbarWrapper = styled.header`
  position: relative;
  width: 100%;
  height: ${({ $isLoggedIn }) => ($isLoggedIn ? '35px' : '0px')};

  overflow: hidden;

  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  transition: height 0.3s ease;
`;

export const PositionWrapper = styled.div`
  position: absolute;
  bottom: 0;
  left: 0;

  display: flex;
  align-items: center;

  width: 100%;
  height: 35px;

  background-color: #010409;
  border-bottom: 1px solid #30363d;
`;

export const MenuButton = styled.button`
  margin: auto 0;
`;

export const SelectedGround = styled.h1`
  display: inline-block;

  font-size: 1.3rem;
  cursor: pointer;
`;
