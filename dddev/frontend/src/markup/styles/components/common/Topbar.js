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

  border-bottom: 1px solid var(--border-basic);
`;

export const PositionWrapper = styled.div`
  position: absolute;
  bottom: 0;
  left: 0;

  display: flex;
  align-items: center;
  width: 100%;
  height: 35px;

  background-color: var(--bg-dark);
`;

export const MenuButton = styled.button`
  margin: auto 0;
`;

export const SelectedGround = styled.h1`
  display: inline-block;

  color: var(--font-rev);
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
  width: 100px;
  height: 50px;

  //비율 유지
  object-fit: cover;

  transition: filter 0.3s ease;
  border-radius: 25px 0 0 25px;
  cursor: pointer;
`;

export const PorfileButtonWrapper = styled.div`
  position: absolute;
  top: -8px;
  right: 0;
  width: 100px;
  height: 50px;

  &:hover {
    > img {
      filter: brightness(0.5);
    }

    > svg {
      opacity: 1;
    }
  }

  & > svg {
    position: absolute;
    width: 30px;
    height: 30px;

    top: 50%;
    right: 50%;

    transform: translate(50%, -50%) scaleX(-1);
    transition: opacity 0.3s ease;
    opacity: 0;

    cursor: pointer;
  }
`;
