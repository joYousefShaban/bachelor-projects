-------------------------------------------------------------------------------
--
-- Title       : programMemory
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : PROGRAMMEMORY.vhd
-- Generated   : Fri Feb  4 02:29:39 2022
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
--{entity {programMemory} architecture {programMemory}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity programMemory is
	 port(
		 address : in STD_LOGIC_VECTOR(7 downto 0);
		 instruction : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end programMemory;

--}} End of automatically maintained section

architecture programMemory of programMemory is
begin

	with address select
	instruction <= 	 
--	X"2002000a" when X"00",	 
--	X"20030014" when X"01",
--	X"00430820" when X"02",
--	X"ac010000" when X"03",
--	X"8c020000" when X"04",
	--program mips2.asm
--	X"2002000a" when X"00",
--	X"20030014" when X"01",
--	X"00430820" when X"02",
--	X"ac010000" when X"03",
--	X"8c020000" when X"04",
--program mips3.asm
--	X"2002000a" when X"00",
--	X"20030014" when X"01",
--	X"00430820" when X"02",
--	X"ac010000" when X"03",
--	X"8c000000" when X"04",	   
--	X"2002001e" when X"05",
--	X"20030028" when X"06",
--	X"00430820" when X"07",
--	X"ac010001" when X"08",
--	X"8c000001" when X"09",
--	X"8c010000" when X"0a",
--	X"8c020001" when X"0b",
--	X"00221820" when X"0c",
--	X"ac030002" when X"0d",
--	X"8c000002" when X"0e",	   
--program mips4.asm
--	X"20020064" when X"00",
--	X"20010000" when X"01",
--	X"ac210000" when X"02",
--	X"20210001" when X"03",
--	X"1422fffd" when X"04",
--	X"8c000020" when X"05",
--	X"08000006" when X"06",
--program mipsandimpl.asm
--X"20010064" when X"00",
--X"2002000f" when X"01",
--X"00221824" when X"02",
--X"ac030000" when X"03",
--X"8c000000" when X"04",
--X"08000005" when X"05",
--					   
X"20010005" when X"00",
X"2022000a" when X"01",
X"34410064" when X"02",
X"30220000" when X"03",
X"ac220000" when X"04",
X"8c420000" when X"05",

	(OTHERS=>'0') when others;

end programMemory;
