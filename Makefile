# Specify the main target
TARGET = dd2ft-test

# Default build type
TYPE = debug

# Which directories contain source files
DIRS = src

OS := $(shell uname -s)

#Preprocessor definitions
CPPDEFS = 

CCPARAM = -Wall -pthread $(CFLAGS) 

# The next blocks change some variables depending on the build type
ifeq ($(TYPE),debug)
    ifeq ($(OS),Darwin)
LDPARAM = 
    else
LDPARAM = -rdynamic -Wl,-warn-unresolved-symbols
    endif
CCPARAM += -g
CPPDEFS += DEBUG
endif

ifeq ($(TYPE),profile)
LDPARAM = --export-dynamic -pg /lib/libc.so.5
CCPARAM += -pg 
endif

ifeq ($(TYPE), release)
LDPARAM = --export-dynamic -s
CCPARAM += -O3
endif

LDPARAM += $(LDFLAGS)

# Which libraries are linked

LIBS := stdc++

PKG_LIBS := 

# Static libraries
STATIC_LIBS = 
# Dynamic libraries
DLIBS =

# Add directories to the include and library paths
INCPATH :=  $(DIRS) /opt/local/include

PKG_INC := 

LIBPATH :=  /opt/local/lib/ /usr/local/lib 

# Branch customizations



# Which files to add to backups, apart from the source code
EXTRA_FILES = Makefile
# The compiler
C++ = g++

# Where to store object and dependancy files.
STORE = build/$(TYPE)
# Makes a list of the source (.cpp) files.
SOURCE := $(foreach DIR,$(DIRS),$(wildcard $(DIR)/*.cpp))
CSOURCE := $(foreach DIR,$(DIRS),$(wildcard $(DIR)/*.c))
# List of header files.
HEADERS := $(foreach DIR,$(DIRS),$(wildcard $(DIR)/*.h))
# Makes a list of the object files that will have to be created.
OBJECTS := $(addprefix $(STORE)/, $(SOURCE:.cpp=.o))
COBJECTS := $(addprefix $(STORE)/, $(CSOURCE:.c=.o))
# Same for the .d (dependancy) files.
DFILES := $(addprefix $(STORE)/,$(SOURCE:.cpp=.d))
CDFILES := $(addprefix $(STORE)/,$(CSOURCE:.c=.d))


# Specify phony rules. These are rules that are not real files.
.PHONY: clean backup dirs

# Main target. The @ in front of a command prevents make from displaying
# it to the standard output.
#$(TARGET):
$(TARGET): dirs $(OBJECTS) $(COBJECTS)
	@echo Linking $(OBJECTS)...
	$(C++) -o $(STORE)/$(TARGET) $(OBJECTS) $(COBJECTS) $(LDPARAM) $(foreach LIBRARY, $(LIBS),-l$(LIBRARY)) $(foreach LIB,$(LIBPATH),-L$(LIB)) $(PKG_LIBS) $(STATIC_LIBS)


# Rule for creating object file and .d file, the sed magic is to add
# the object path at the start of the file because the files gcc
# outputs assume it will be in the same dir as the source file.
$(STORE)/%.o: %.cpp
	@echo Creating object file for $*...
	$(C++) -Wp,-MMD,$(STORE)/$*.dd $(CCPARAM) $(foreach INC,$(INCPATH),-I$(INC)) $(PKG_INC)\
		$(foreach CPPDEF,$(CPPDEFS),-D$(CPPDEF)) -c $< -o $@
	@sed -e '1s/^\(.*\)$$/$(subst /,\/,$(dir $@))\1/' $(STORE)/$*.dd > $(STORE)/$*.d
	@rm -f $(STORE)/$*.dd

$(STORE)/%.o: %.c
	@echo Creating object file for $*...
	$(C++) -Wp,-MMD,$(STORE)/$*.dd $(CCPARAM) $(foreach INC,$(INCPATH),-I$(INC)) $(PKG_INC)\
		$(foreach CPPDEF,$(CPPDEFS),-D$(CPPDEF)) -c $< -o $@
	@sed -e '1s/^\(.*\)$$/$(subst /,\/,$(dir $@))\1/' $(STORE)/$*.dd > $(STORE)/$*.d
	@rm -f $(STORE)/$*.dd

# Empty rule to prevent problems when a header is deleted.
%.h: ;

# Cleans up the objects, .d files and executables.
clean:
	@echo Making clean.
	@-rm -f $(foreach DIR,$(DIRS),$(STORE)/$(DIR)/*.d $(STORE)/$(DIR)/*.o)
	@-rm -f $(TARGET)

# Backup the source files.
backup:
	@-if [ ! -e build/backup ]; then mkdir -p build/backup; fi;
	@zip build/backup/backup_`date +%d-%m-%y_%H.%M`.zip $(SOURCE) $(HEADERS) $(EXTRA_FILES)

# Create necessary directories
dirs:
	@-if [ ! -e $(STORE) ]; then mkdir -p $(STORE); fi;
	@-$(foreach DIR,$(DIRS), if [ ! -e $(STORE)/$(DIR) ]; then mkdir $(STORE)/$(DIR); fi; )

# Includes the .d files so it knows the exact dependencies for every
# source.
-include $(DFILES)
-include $(CDFILES)

