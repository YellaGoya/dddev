import styled from 'styled-components';

export const GroundWrapper = styled.div`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 375px;

  color: var(--font-rev);
`;

export const Title = styled.h1`
  text-align: center;
`;

export const TextButton = styled.button`
  display: block;
  margin: 0 auto;

  border: none;
  background-color: transparent;
  color: var(--font-rev);
  font-size: 1rem;

  cursor: pointer;
`;

export const TextLink = styled.a`
  display: block;

  font-size: 1rem;
  color: var(--font-rev);
  text-decoration: none;
  text-align: center;
  cursor: pointer;
`;
