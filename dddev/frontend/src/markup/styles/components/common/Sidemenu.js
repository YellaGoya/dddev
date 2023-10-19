import styled from 'styled-components';
import { Link } from 'react-router-dom';

export const SidemenuWrapper = styled.aside`
  position: relative;
  z-index: 2;

  width: ${({ $menuToggle }) => ($menuToggle ? '275px' : '0px')};
  flex-shrink: 0;

  overflow: hidden;

  transition: width 0.5s ease;
`;

export const PositionWrapper = styled.div`
  position: absolute;
  top: 0;
  right: 0;

  width: 275px;
  height: 100%;

  background-color: aqua;
`;

export const MenuNav = styled.nav``;

export const MenuCategory = styled(Link)`
  display: block;
`;

export const MenuChild = styled(Link)`
  display: block;
`;

export const GroundList = styled.ul`
  position: absolute;

  display: ${({ $groundListToggle }) => ($groundListToggle ? 'block' : 'none')};
  top: 43px;
  left: 15px;
  width: 245px;

  background-color: white;
`;

export const GroundItem = styled.li`
  cursor: pointer;
`;
