-------------------------------------------------------------------------------
--
-- Title       : CPU
-- Design      : systemv1
-- Author      : Owner
-- Company     : UPIT
--
-------------------------------------------------------------------------------
--
-- File        : CPU.vhd
-- Generated   : Fri Feb  4 03:25:32 2022
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
--{entity {CPU} architecture {CPU_a}}

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity CPU is
	 port(
		 clk : in STD_LOGIC;
		 reset : in STD_LOGIC;
		 dataMemRdOut : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end CPU;

--}} End of automatically maintained section

architecture CPU_a of CPU is	
component REGISTERFILE is 
	 port(
		 clk : in STD_LOGIC;
		 comWr : in STD_LOGIC;
		 DatWr : in STD_LOGIC_VECTOR(31 downto 0);
		 comWrSel : in STD_LOGIC_VECTOR(1 downto 0);  
		 comRdCh1Sel : in std_logic_vector(1 downto 0);
		 comRdCh2Sel : in std_logic_vector(1 downto 0);
		 DatRdCh1 : out STD_LOGIC_VECTOR(31 downto 0);
		 DatRdCh2 : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end component;
component selection is
	 port(
		 comSelect : in STD_LOGIC;
		 I0 : in STD_LOGIC_VECTOR(31 downto 0);
		 I1 : in STD_LOGIC_VECTOR(31 downto 0);
		 Y : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end component;
component selection3Inputs is
	 port(
		 I0 : in STD_LOGIC_VECTOR(31 downto 0);
		 I1 : in STD_LOGIC_VECTOR(31 downto 0);
		 I2 : in STD_LOGIC_VECTOR(31 downto 0);
		 sel : in STD_LOGIC_VECTOR(1 downto 0);
		 Y : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end component;
component ALU is
	 port(
		 comOperation : in STD_LOGIC_VECTOR(1 downto 0);	 --UPDATE HERE	extend to 2 bits for and operation
		 operand1 : in STD_LOGIC_VECTOR(31 downto 0);
		 operand2 : in STD_LOGIC_VECTOR(31 downto 0);
		 zero : out STD_LOGIC;
		 sign : out STD_LOGIC;
		 result : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end component;
component datamemory is
	 port(
		 rd : in STD_LOGIC;
		 wr : in STD_LOGIC;
		 cs : in STD_LOGIC;
		 clk : in std_logic; --write sync
		 address : in STD_LOGIC_VECTOR(7 downto 0);
		 dataIn : in STD_LOGIC_VECTOR(31 downto 0);
		 dataOut : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end component;
component dataMemoryInterface is
	 port(
		 addrIn : in STD_LOGIC_VECTOR(31 downto 0);
		 addrOut : out STD_LOGIC_VECTOR(7 downto 0)
	     );
end component;
component programCounter is
	 port(
		 clk : in STD_LOGIC;
		 reset : in STD_LOGIC;
		 NormalJump : in std_logic_vector(1 downto 0);
		 Z : in STD_LOGIC;
		 jumpAddress: std_logic_vector(7 downto 0);
		 instructionAddress : out STD_LOGIC_VECTOR(7 downto 0)
	     );
end component;
component programMemory is
	 port(
		 address : in STD_LOGIC_VECTOR(7 downto 0);
		 instruction : out STD_LOGIC_VECTOR(31 downto 0)
	     );
end component;
component instructionDecoder is
	 port(
	 	instruction : in STD_LOGIC_VECTOR(31 downto 0);
	 	--registers file com
	 	comWr : out STD_LOGIC;
	 	comWrSel : out STD_LOGIC_VECTOR(1 downto 0);  
	 	comRdCh1Sel : out std_logic_vector(1 downto 0);
		comRdCh2Sel : out std_logic_vector(1 downto 0);	 
		--selection com
		comSelection1 : out std_logic_vector(1 downto 0);
		comSelection2 : out std_logic;
		--alu com
		comOperation : out STD_LOGIC_VECTOR(1 downto 0);--UPDATE HERE extend to 2 bits for and instruction	
		--data memory com
		 rd : out STD_LOGIC;
		 wr : out STD_LOGIC;
		 cs : out STD_LOGIC;	
		 -- PC com
		 NormalJump : out std_logic_vector(1 downto 0);
		 immediateValue : out std_logic_vector(15 downto 0)
	     );
end component; 

--signals
signal DatWr, DatRdCh1, DatRdCh2, Operator2, Result, DatMemWr, DatMemRd, DatSignal : std_logic_vector(31 downto 0);   
signal Address : std_logic_vector(7 downto 0);
signal Z, Sign : std_logic;
	 	--registers file com
signal comWr : STD_LOGIC;
signal 	comWrSel : STD_LOGIC_VECTOR(1 downto 0);  
signal comRdCh1Sel : std_logic_vector(1 downto 0);
signal comRdCh2Sel : std_logic_vector(1 downto 0);	 
		--selection com
signal comSelection1 : std_logic_vector(1 downto 0);
signal comSelection2 : std_logic;
		--alu com
signal comOperation : STD_LOGIC_VECTOR(1 downto 0);	--UPDATE HERE extend to 2 bits size for and operation
		--data memory com
signal	 rd : STD_LOGIC;
signal	 wr : STD_LOGIC;
signal	 cs : STD_LOGIC;	
		 -- PC com
signal NormalJump : std_logic_vector(1 downto 0);
signal immediateValue : std_logic_vector(31 downto 0);


signal Instruction : std_logic_vector(31 downto 0);
signal InstructionAddress : std_logic_vector(7 downto 0);

begin

	cselection1: selection3Inputs port map(Result,DatMemRd,DatSignal,comSelection1,DatWr);			 
	cselection2: selection port map(comSelection2,DatRdCh2,immediateValue,Operator2);
	cregfile: REGISTERFILE port map(clk,comWr,DatWr,comWrSel,comRdCh1Sel,comRdCh2Sel,DatRdCh1,DatRdCh2);
	calu: ALU port map(comOperation,DatRdCh1,Operator2,Z,Sign,Result);
	cdatameminterf: dataMemoryInterface port map(Result,Address);
	cdatamem: datamemory port map(rd,wr, cs, clk, Address,DatMemWr,DatMemRd);
	cPC: programCounter port map(clk,reset,NormalJump,Z,immediateValue(7 downto 0),InstructionAddress);
	cprogmem: programMemory port map( InstructionAddress,Instruction);
	cinstructionDecoder : instructionDecoder port map(Instruction,comWr,comWrSel,comRdCh1Sel,comRdCh2Sel,comSelection1,comSelection2,comOperation,rd,wr,cs,NormalJump,immediateValue(15 downto 0));
	dataMemRdOut <=  DatMemRd;
	DatSignal <= X"0000000"&"000"&Sign;	
	immediateValue(31 downto 16) <= (OTHERS=>'0');	   
	DatMemWr <= DatRdCh2;
	
end CPU_a;
