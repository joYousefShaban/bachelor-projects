-------------------------------------------------------------------------------
--
-- Title       : dataMemoryInterface
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : DATAMEMORYINTERFACE.vhd
-- Generated   : Fri Feb  4 02:17:19 2022
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
--{entity {dataMemoryInterface} architecture {dataMemoryInterface_a}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity dataMemoryInterface is
	 port(
		 addrIn : in STD_LOGIC_VECTOR(31 downto 0);
		 addrOut : out STD_LOGIC_VECTOR(7 downto 0)
	     );
end dataMemoryInterface;

--}} End of automatically maintained section

architecture dataMemoryInterface_a of dataMemoryInterface is
begin

	--just convert address from 32 bits to 8 bits
	addrOut<= addrIn(7 downto 0);

end dataMemoryInterface_a;
