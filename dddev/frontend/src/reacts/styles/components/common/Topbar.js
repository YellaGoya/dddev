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
  background-color: var(--bg-dark);

  border-bottom: 1px solid var(--border-basic);
`;

export const PositionWrapper = styled.span`
  position: absolute;
  bottom: 0;
  left: 0;

  display: flex;
  align-items: center;
  width: calc(100% - 110px);
  height: 35px;

  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  & > :nth-child(2) {
    margin-left: ${({ $isGround }) => ($isGround ? '6px' : '0')};
  }

  & > :nth-child(3) {
    margin-left: 6px;
  }
`;

export const MenuButton = styled.button`
  margin: auto 0;
`;

export const SelectedGround = styled.span`
  display: inline;

  color: var(--font-rev);
  font-size: 1rem;
  font-weight: 500;

  display: flex;
  align-items: center;
  justify-content: center;

  pointer-events: ${({ $available }) => ($available ? 'none' : 'auto')};
  opacity: ${({ $available }) => ($available ? '0.5' : '1')};
  cursor: pointer;

  & > svg {
    margin: 0 10px;
  }
`;

export const PathsText = styled.span`
  display: inline;
  width: auto;

  color: var(--font-rev);
  font-size: 0.9rem;
  font-weight: 300;

  display: flex;
  align-items: center;
  justify-content: center;
`;

export const PorfileButtonWrapper = styled.div`
  position: absolute;
  top: 2px;
  right: 15px;
  width: 90px;
  height: 30px;

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

export const ProfileImage = styled.img`
  width: 90px;
  height: 30px;

  //비율 유지
  object-fit: cover;

  border: 1px solid var(--border-dark);
  transition: filter 0.3s ease;
  border-radius: 25px;
  cursor: pointer;
`;

export const GradBox = styled.div`
  position: fixed;
  height: 34px;
  width: 15px;

  top: 0;
  right: 110px;

  background: linear-gradient(90deg, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 1) 100%);
`;
