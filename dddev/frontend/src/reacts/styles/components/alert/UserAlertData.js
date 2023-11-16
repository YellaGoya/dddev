import styled from 'styled-components';

export const UserAlertList = styled.ul`
  list-style: none;
  padding: 15px;
  margin-bottom: 15px;

  overflow-x: hidden;
  overflow-y: auto;

  max-height: 340px;
`;

export const UserAlertItem = styled.li`
  padding: 10px 10px;

  border-radius: 0.25rem;
  &:hover {
    background-color: var(--bg-lite);
  }
`;

export const AlertTag = styled.span`
  padding: 3px 6px;
  background-color: var(--button-lite);
  border-radius: 0.25rem;
  font-size: 0.9rem;
  margin: 0 5px;
  line-height: 1.8rem;
`;

export const AlertContent = styled.p`
  padding: 0 5px;
  margin-top: 13px;
  margin-bottom: 0;

  color: ${({ $read }) => ($read ? 'var(--font-border)' : 'var(--font-rev)')};

  &:hover {
    text-decoration: underline;
    text-underline-offset: 3px;

    cursor: pointer;
  }
`;
