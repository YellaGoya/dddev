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
  left: 50%;
  width: calc(100% - 20px);
  max-width: 500px;
  min-width: 355px;
  aspect-ratio: 3 / 4;

  @media (min-height: 755px) {
    top: 50%;
    transform: translate(-50%, -50%);
  }

  @media (max-height: 754px) {
    top: 10px;
    transform: translateX(-50%);
  }

  border-radius: 0.75rem;
  border: 1px solid var(--border-basic);

  background-color: var(--bg-dark);
`;

export const EditImageWrapper = styled.div`
  position: relative;

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
  width: calc(100% - 20px);
  height: fit-content;
  margin: 0 10px;

  object-fit: cover;

  border-radius: 0.5rem;
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

export const ProfileEditButton = styled.button`
  padding: 0%;
  background-color: transparent;
  border: none;
  cursor: pointer;
`;
