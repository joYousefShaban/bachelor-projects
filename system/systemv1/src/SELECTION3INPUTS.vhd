-------------------------------------------------------------------------------
--
-- Title       : selection3Inputs
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : SELECTION3INPUTS.vhd
-- Generated   : Fri Feb  4 02:41:45 2022
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
--{entity {selection3Inputs} architecture {selection3Inputs_a}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity selection3Inputs is
	 port(
		 I0 : in STD_LOGIC_VECTOR(31 downto 0);
		 I1 : in STD_LOGIC_VECTOR(31 downto 0);
		 I2 : in STD_LOGIC_VECTOR(31 downto 0);
		 sel : in STD_LOGIC_VECTOR(1 downto 0);
		 Y : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end selection3Inputs;

--}} End of automatically maintained section

architecture selection3Inputs_a of selection3Inputs is
begin

	with sel select
	Y <= I0 when "00",
	I1 when "01",
	I2 when "10",
	(OTHERS=>'0') when others;

end selection3Inputs_a;
