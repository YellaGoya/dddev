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
  max-width: 600px;
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
