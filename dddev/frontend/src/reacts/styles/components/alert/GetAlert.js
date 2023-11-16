import styled from 'styled-components';

export const OutWrapper = styled.div`
  background-color: inherit;
`;

export const AlarmTitle = styled.h3`
  padding: 0 30px;
  margin-bottom: 10px;
`;

export const ButtonWrapper = styled.div`
  width: 100%;
  height: fit-content;
  padding: 0 25px 0 25px;

  display: flex;
  align-items: center;
`;

export const Button = styled.button`
  position: relative;
  width: calc(40% - 17px);
  height: 40px;
  padding: 0;

  border-radius: 0.75rem;
  margin-bottom: 25px;

  cursor: pointer;

  color: ${({ $toggle }) => ($toggle ? 'var(--font-basic)' : 'var(--font-rev)')};

  border: 1px solid var(--border-basic);
  background-color: ${({ $toggle }) => ($toggle ? 'var(--font-toggle)' : 'var(--bg-basic)')};

  transition: all 0.2s ease;

  &:hover {
    background-color: ${({ $toggle }) => ($toggle ? 'var(--font-toggle-hover)' : 'var(--bg-lite)')};
    /* background-color: var(--bg-lite); */
  }

  &:nth-child(2) {
    margin-left: 10px;
    margin-right: 24px;
  }

  &:nth-child(3) {
    width: 20%;
  }

  & > svg {
    position: absolute;
    right: 15px;
    top: 50%;
    transform: translateY(-50%);
  }
`;
