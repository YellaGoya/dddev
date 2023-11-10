import styled from 'styled-components';

export const SelectWrapper = styled.div`
  position: relative;
  margin-bottom: 1.5rem;
`;

export const Label = styled.div`
  position: absolute;
  left: 40px;
  top: -9px;
  padding: 0 5px;
  color: var(--font-border);
  background-color: var(--bg-dark);

  font-weight: 500;
  font-size: 0.8rem;
`;

export const Selected = styled.p`
  width: calc(100% - 50px);
  height: 55px;
  margin: 0 25px;

  border-radius: 0.75rem;
  border: 1px solid ${({ $isActive }) => ($isActive ? 'var(--border-focus)' : 'var(--border-basic)')};
  background-color: transparent;

  font-size: 0.9rem;

  color: var(--font-rev);

  display: flex;
  align-items: center;

  padding: 0 20px;
  &:focus {
    outline: none;
  }
`;

export const SelectList = styled.ul`
  position: absolute;
  top: 56px;
  width: calc(100% - 50px);
  margin: 0 25px;
  padding: 18px 20px;
  max-height: 200px;

  overflow-y: auto;

  border-radius: 0.75rem;
  border: 1px solid ${({ $isActive }) => ($isActive ? 'var(--border-focus)' : 'var(--border-basic)')};
  background-color: #111d2e;

  list-style: none;

  z-index: 5;
`;

export const SelectItem = styled.li`
  color: var(--font-rev);
  font-size: 0.9rem;
`;
