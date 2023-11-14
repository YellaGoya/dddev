import Modal from 'react-modal';
import styled from 'styled-components';

export const ModalWrapper = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #000;

  display: ${({ $isOpen }) => ($isOpen ? 'block' : 'none')};
`;
export const ModalChanger = styled(Modal)`
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background: var(--bg-basic);
  color: #000;
  width: fit-content;
  max-width: 500px;
  min-width: 355px;
  padding: 10px 20px 45px 20px;

  border-radius: 0.75rem;
  box-shadow:
    rgba(0, 0, 0, 0.24) 0px 1px 1px,
    rgba(0, 0, 0, 0.24) 0px 2px 2px,
    rgba(0, 0, 0, 0.24) 0px 4px 6px;

  & > p {
    color: var(--font-rev);
    text-align: center;
  }
`;

export const Button = styled.button`
  position: absolute;
  right: 20px;
  bottom: 15px;

  padding: 5px 10px;
  border: none;
  background-color: transparent;

  border-radius: 0.5rem;

  cursor: pointer;

  &:hover {
    background-color: var(--bg-lite-hover);
  }

  font-size: 0.9rem;
  font-weight: 500;
  x &.accept-button {
  }
`;
