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

  background-color: ${(props) => (props.$toggle ? 'rgba(255, 255, 255, 0.05)' : 'rgba(0, 0, 0, 0)')};
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

  background-color: var(--bg-basic);
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

  & > div {
    width: calc(100% + 9px);
  }

  & > :first-child {
    margin: 30px 0 20px 0;
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

export const ButtonWrapper = styled.span`
  position: relative;
  display: block;
  width: calc(100% - 40px);
  margin: 0 25px 30px 25px;
  height: 21px;

  & > button {
    position: absolute;
    bottom: 0;
    display: block;
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
