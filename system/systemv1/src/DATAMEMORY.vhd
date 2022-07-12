-------------------------------------------------------------------------------
--
-- Title       : datamemory
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : DATAMEMORY.vhd
-- Generated   : Fri Feb  4 02:09:22 2022
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
--{entity {datamemory} architecture {datamemory_a}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity datamemory is
	 port(
		 rd : in STD_LOGIC;
		 wr : in STD_LOGIC;
		 cs : in STD_LOGIC;
		 clk : in std_logic; --write sync
		 address : in STD_LOGIC_VECTOR(7 downto 0);
		 dataIn : in STD_LOGIC_VECTOR(31 downto 0);
		 dataOut : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end datamemory;

--}} End of automatically maintained section

architecture datamemory_a of datamemory is 
type memoryContent is array(0 to 255) of std_logic_vector(31 downto 0);
begin

	process(clk, cs, rd, wr, address)
	variable content : memoryContent;
	variable index : integer;
	begin	
		if cs = '1' then			 
			index := 0;
			--convert address in integer
			for i in 0 to 7 loop
				if address(i) = '1' then
					index := index + 2**i;
				end if;
			end loop;
			if rd = '1' then 
				dataOut <= content(index);
			else
				dataOut <= (OTHERS=>'0');
			end if;
			if wr = '1' then
				if clk'event and clk='0' then --rising edge
					content(index) := dataIn;
				end if;
			end if;
		else
			dataOut <= (OTHERS=>'0');
		end if;
	end process;

end datamemory_a;
