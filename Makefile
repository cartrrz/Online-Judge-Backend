CC=g++
CFLAGS=-c -std=c++11 -Wall -fPIC -I "{$JAVA_HOME}include/"
LDFLAGS=-fPIC -shared
EXTFLAGS=


SOURCES_DIR=gateway/src/main/cpp/unix
OBJECTS_DIR=gateway/target/cpp
EXECUTABLE=target/classes/libJudgerCore.dylib
EXTFLAGS=-lpthread -lrt

SOURCES=$(wildcard $(SOURCES_DIR)/*.cpp)
OBJECTS=$(SOURCES:$(SOURCES_DIR)/%.cpp=$(OBJECTS_DIR)/%.o)

all: $(EXECUTABLE)

$(EXECUTABLE): $(OBJECTS)
	$(CC) $(LDFLAGS) $(OBJECTS) $(EXTFLAGS) -o $@

$(OBJECTS): $(SOURCES)
	mkdir -p $(OBJECTS_DIR)
	$(CC) $(CFLAGS) $< -o $@

clean:
	rm -rf $(OBJECTS_DIR) $(EXECUTABLE)
