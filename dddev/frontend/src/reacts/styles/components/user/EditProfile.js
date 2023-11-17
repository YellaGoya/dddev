import styled from 'styled-components';

export const OutWrapper = styled.div`
  position: fixed;
  width: 100vw;
  height: 35px;

  display: ${(props) => (props.$toggle ? 'block' : 'none')};

  top: 0;
  left: 0;

  opacity: 0;
`;

export const EditWrapper = styled.div`
  position: fixed;
  width: 100vw;
  height: calc(100vh - 35px);

  top: 35px;
  left: 0px;

  visibility: ${(props) => (props.$toggle ? 'visible' : 'hidden')};

  background-color: ${(props) => (props.$toggle ? 'rgba(255, 255, 255, 0.05);' : 'rgba(0, 0, 0, 0)')};
  backdrop-filter: blur(3px);
  z-index: 100;

  transition: background-color 0.3s ease;
`;

export const EditModalWrapper = styled.div`
  position: fixed;
  top: 50px;
  left: 50%;
  width: calc(100% - 20px);
  max-width: 500px;
  min-width: 355px;
  height: fit-content;
  transform: translateX(-50%);

  border-radius: 0.75rem;
  overflow: hidden;

  background-color: var(--bg-modal);
  border: 1px solid var(--border-setting);

  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;
`;

export const GradBoxWrapper = styled.div`
  width: 100%;
  height: fit-content;
  max-height: calc(100vh - 140px);
  min-height: 366px;

  background-color: inherit;

  & > div {
    width: calc(100% + 9px);
  }

  & > :first-child {
    margin-top: 20px;
  }

  overflow-x: hidden;
  overflow-y: scroll;

  &:hover {
    &::-webkit-scrollbar-thumb {
      background-color: var(--thumb-hover);
    }

    &::-webkit-scrollbar-thumb:hover {
      background-color: var(--font-rev);
    }
  }
`;

export const EditImageWrapper = styled.div`
  position: relative;
  margin: 25px auto;

  margin-top: 0%;

  width: calc(100% - 50px);

  overflow: hidden;
  border-radius: 0.75rem;

  svg {
    width: 100%;
    height: 100%;
  }

  & > :nth-child(2) {
    transform: translate(-50%, -50%);

    width: 20%;
    aspect-ratio: 1;
  }

  & > :nth-child(3) {
    transform: translate(150%, -50%);

    width: 8%;
    aspect-ratio: 1;
  }

  & > img {
    transition: filter 0.3s ease;
  }

  &:hover {
    > :nth-child(2) {
      opacity: 1;
    }

    > :nth-child(3) {
      opacity: 0.6;

      &:hover {
        opacity: 1;
      }
    }
    > img {
      filter: brightness(0.5) blur(1px);
    }
  }
`;

export const ProfileImage = styled.img`
  width: 100%;

  border-radius: 0.75rem;

  object-fit: cover;
`;

export const MessageWrapper = styled.div`
  position: relative;
`;

export const descriptionMessage = styled.p`
  position: absolute;
  height: fit-content;
  right: 35px;
  bottom: -32px;

  font-size: 0.8rem;
  color: var(--font-warning);
`;

export const ImageButton = styled.button`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translateY(-50%);
  opacity: 0;

  transition: opacity 0.3s ease;

  padding: 0%;
  background-color: transparent;
  border: none;
  cursor: pointer;
`;

export const ButtonWrapper = styled.span`
  position: relative;
  display: block;
  width: calc(100% - 40px);
  margin: 0 25px 30px 25px;
  height: 21px;

  & > button {
    position: absolute;
    bottom: 0;
    display: flex;
    align-items: center;
    padding: 0%;

    font-size: 1rem;
    background-color: transparent;
    border: none;
    cursor: pointer;
  }
`;

export const ProfileEditButton = styled.button`
  right: 50px;
  font-weight: 600;
`;

export const CloseButton = styled.button`
  right: 2px;
  font-weight: 300;
  opacity: 0.9;
`;

export const LogoutButton = styled.button`
  left: 0px;
  height: 21px;
  font-weight: 300;
  opacity: 0.9;

  & > svg {
    display: inline-block;
    width: 21px;
    height: 21px;
  }
`;

export const DivLine = styled.span`
  position: relative;
  display: block;
  width: calc(100% - 50px);
  height: 1px;
  margin: 50px 30px 50px 30px;
  background-color: var(--border-basic);
  opacity: 0.5;
`;

export const GradBoxTop = styled.div`
  position: absolute;
  width: calc(100% - 40px);
  max-width: 470px;
  min-width: 325px;
  top: 0px;
  left: 50%;
  transform: translateX(-50%);

  height: 10px;

  background: linear-gradient(0deg, var(--bg-basic-grad-bottom) 0%, var(--bg-basic-grad-top) 100%);
`;

export const GradBoxBottom = styled.div`
  position: absolute;
  width: calc(100% - 40px);
  max-width: 470px;
  min-width: 325px;
  bottom: 0px;
  left: 50%;
  transform: translateX(-50%);

  height: 10px;

  background: linear-gradient(180deg, var(--bg-basic-grad-top) 0%, var(--bg-basic-grad-bottom) 100%);
`;
