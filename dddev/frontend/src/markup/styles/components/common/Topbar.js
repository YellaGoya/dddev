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

  border-bottom: 1px solid #30363d;
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
`;

export const MenuButton = styled.button`
  margin: auto 0;
`;

export const SelectedGround = styled.h1`
  display: inline-block;

  color: #fff;
  font-size: 1.3rem;

  display: flex;
  align-items: center;
  justify-content: center;

  cursor: pointer;

  & > svg {
    transform: translateY(0px);
    margin: 0 10px;
  }
`;

export const ProfileImage = styled.img`
  position: absolute;
  top: -8px;
  right: 0;
  width: 100px;
  height: 50px;

  //비율 유지
  object-fit: cover;

  border-radius: 25px 0 0 25px;
  cursor: pointer;
`;
