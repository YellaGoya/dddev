import styled from 'styled-components';

export const SelectWrapper = styled.div`
  position: relative;
  margin-bottom: 1.5rem;

  pointer-events: ${({ $available }) => ($available ? 'auto' : 'auto')};
  opacity: ${({ $available }) => ($available ? '1' : '0.5')};
`;

export const Label = styled.h1`
  padding: 0 5px;
  color: var(--font-border);

  width: fit-content;
  margin-right: 5px;

  font-weight: 500;
  font-size: 0.8rem;
  white-space: nowrap;

  pointer-events: none;
`;

export const Selected = styled.div`
  width: calc(100% - 50px);
  height: 40px;
  margin: 0 25px;

  border-radius: 0.75rem;
  background-color: ${({ $toggle }) => ($toggle ? 'var(--button-basic)' : 'transparent')};

  & > p {
    font-size: 0.9rem;
    color: var(--font-rev);

    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;

    pointer-events: none;
  }

  display: flex;
  align-items: center;

  padding: 0 47px 0 15px;
  &:focus {
    outline: none;
  }

  &:hover {
    background-color: var(--button-basic);
  }

  & > svg {
    position: absolute;
    right: 40px;
    fill: var(--font-rev);

    transform: ${({ $toggle }) => ($toggle ? 'rotate(180deg)' : 'rotate(360deg)')};
    fill: ${({ $toggle }) => ($toggle ? 'var(--font-toggle)' : 'var(--font-rev)')};

    transition: all 0.4s ease;
    pointer-events: none;
  }

  cursor: pointer;
`;

export const SelectList = styled.ul`
  position: relative;
  width: calc(100% - 50px);
  margin: 16px 25px 8px 25px;
  padding: 0 10px 0 20px;

  max-height: ${({ $toggle }) => ($toggle ? '200px' : '0px')};

  overflow-y: scroll;

  border-radius: 0.75rem;

  list-style: none;

  transition: all 0.7s ease;
`;

export const SelectItem = styled.li`
  height: 30px;
  display: flex;
  align-items: center;

  margin-bottom: 2px;
  padding: 0 10px;
  border-radius: 0.5rem;

  & > p {
    color: var(--font-rev);
    font-size: 0.9rem;

    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
    pointer-events: none;
  }

  &:hover {
    background-color: var(--font-toggle);
    color: var(--bg-basic);
  }
`;
