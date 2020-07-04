JFLAGS = -g
JC = javac
JVM = java

FILES = chess/Engine \
		chess/GameInstance \
		chess/MainClass \
		chess/Values \
		pieces/Bishop \
		pieces/King \
		pieces/Knight \
		pieces/Pawn \
		pieces/Piece \
		pieces/Queen \
		pieces/Rook \
		units/Move \
		units/Position \
		units/Promotion \
		units/Square

MAIN_FILE = chess/MainClass

LOG_FILE = logYapalPapal.txt

XDEBUG = xboard.debug
XEXEC = xboard
XSOURCE = .
XRESULT = partide.txt
XCMD = $(XEXEC) -debug -fd $(XSOURCE) -fcp "$(JVM) $(MAIN_FILE)"
XCMD_FAIRY = $(XEXEC) -fcp "$(JVM) $(MAIN_FILE)" -scp "fairymax" -secondInitString "new\nrandom\nsd 3\n" -tc 5 -inc 2 \
				-autoCallFlag true -mg 10 -sgf $(XRESULT) -reuseFirst false -debug

.SUFFIXES: .java .class

.PHONY: default build run clean

.java.class:
	$(JC) $(JFLAGS) $(addsuffix .java,$(FILES))

default: build

build: $(addsuffix .class,$(FILES))

run: build
	$(JVM) $(MAIN_FILE)

run_xboard: build
	$(XCMD)

run_fairymax: build
	$(XCMD_FAIRY)

clean:
	rm -f $(addsuffix .class,$(FILES))
	rm -f $(XDEBUG)
	rm -f $(XRESULT)
	rm -f $(LOG_FILE)