import styled from 'styled-components';

export const MessageBoxModal = styled.div`
  position: fixed;
  top: 45px;
  right: ${({ $toggle }) => ($toggle ? '10px' : 'min(-355px,max(-550px, calc(-100vw - 20px)))')};
  width: calc(100vw - 20px);
  min-width: 355px;
  max-width: 550px;
  border: 1px solid var(--border-basic);

  height: fit-content;
  max-height: calc(100vh - 55px);

  background-color: var(--bg-dark);
  border-radius: 0.75rem;
  z-index: 3;

  visibility: ${({ $toggle }) => ($toggle ? 'visible' : 'hidden')};
  opacity: ${({ $toggle }) => ($toggle ? '1' : '0')};
  color: var(--font-rev);
  transition:
    visibility 0.5s ease,
    right 0.5s ease,
    opacity 0.5s ease;
`;

export const EditProfile = styled.div`
  width: 100%;
  height: 800px;
  background-color: aqua;
`;
