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

  background-color: ${(props) => (props.$toggle ? 'rgba(0, 0, 0, 0.3)' : 'rgba(0, 0, 0, 0)')};
  backdrop-filter: blur(5px);
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
  height: calc(100% - 100px);
  max-height: 800px;
  min-height: 366px;
  aspect-ratio: 3 / 4;
  transform: translateX(-50%);

  & > div {
    width: calc(100% + 9px);
  }

  & > :first-child {
    margin-top: 30px;
  }

  overflow-x: hidden;
  overflow-y: scroll;

  border-radius: 0.75rem;
  border: 1px solid var(--border-basic);

  background-color: var(--bg-dark);
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
