-------------------------------------------------------------------------------
--
-- Title       : selection
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : SELECTION.vhd
-- Generated   : Fri Feb  4 01:57:52 2022
-- From        : interface description file
-- By          : Itf2Vhdl ver. 1.20
--
-------------------------------------------------------------------------------
--
-- Description : 
--
-------------------------------------------------------------------------------

--{{ Section below this comment is automatically maintained
--   and may be overwritten
--{entity {selection} architecture {selectiion_a}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity selection is
	 port(
		 comSelect : in STD_LOGIC;
		 I0 : in STD_LOGIC_VECTOR(31 downto 0);
		 I1 : in STD_LOGIC_VECTOR(31 downto 0);
		 Y : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end selection;

--}} End of automatically maintained section

architecture selectiion_a of selection is
begin

	with comSelect select
	Y <= I0  when '0',
	I1 when '1',
	(OTHERS=>'0') when others;

end selectiion_a;
