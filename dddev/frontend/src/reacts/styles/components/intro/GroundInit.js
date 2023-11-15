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
  margin-bottom: 10px;
`;

export const TextButton = styled.button`
  display: block;
  margin: 0 auto;
  height: 40px;
  padding: 0 15px;

  border: none;
  border-radius: 0.75rem;
  background-color: transparent;
  color: var(--font-rev);
  font-size: 1rem;

  display: flex;
  align-items: center;

  cursor: pointer;

  &:hover {
    background-color: var(--button-basic);
  }

  & > svg {
    margin-left: 5px;
    width: 21px;
    height: 21px;
  }
`;

export const TextLink = styled.a`
  display: block;

  font-size: 1rem;
  color: var(--font-rev);
  text-decoration: none;
  text-align: center;
  cursor: pointer;
`;
