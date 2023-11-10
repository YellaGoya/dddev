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
    margin: 30px 25px 10px 25px;
  }

  overflow-x: hidden;
  overflow-y: scroll;

  border-radius: 0.75rem;
  border: 1px solid var(--border-basic);

  background-color: var(--bg-dark);
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

export const DivLine = styled.span`
  position: relative;
  display: block;
  width: calc(100% - 90px);
  height: 1px;
  margin: 50px;
  background-color: var(--border-basic);
  opacity: 0.5;
`;

export const GradBoxTop = styled.div`
  width: calc(100% - 50px);
  max-width: 470px;
  min-width: 325px;
  position: fixed;
  top: 51px;
  left: 50%;
  transform: translateX(-50%);

  height: 10px;

  background: linear-gradient(0deg, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 1) 100%);
`;

export const GradBoxBottom = styled.div`
  width: calc(100% - 50px);
  max-width: 470px;
  min-width: 325px;
  position: fixed;

  @media (max-height: 935px) {
    top: calc(100vh - 96px);
  }

  @media (max-height: 500px) {
    top: 405px;
  }

  @media (min-height: 936px) {
    top: 839px;
  }
  left: 50%;
  transform: translateX(-50%);

  height: 10px;

  background: linear-gradient(180deg, rgba(0, 0, 0, 0) 0%, rgba(0, 0, 0, 1) 100%);

  /* background-color: #fff; */
`;
