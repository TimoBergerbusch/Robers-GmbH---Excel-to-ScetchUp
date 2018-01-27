#require 'rubyXL'

class ExcelReader
# the row in which the header elements are supposed to be
# Since: 19.01.2018
  $rowOfHeader = 16

# the columns where the header elements are supposed to be
  $indexHeaderMap = {"Lfd" => 13, "Sage Art." => 16, "Bezeichnung Bauteil" => 17, "Materialgruppe / Werkstoff" => 37, "Anz." => 59, "Länge" => 63, "Breite" => 69, "Höhe" => 74}

# the columns where the exact values of an element are supposed to be
# Note: arenÄt the same as the $indexHeaderMap-entries
  $indexValuesMap = {"Lfd" => 13, "Sage Art." => 16, "Bezeichnung" => 17, "Bauteil" => 21, "Materialgruppe" => 37, "Werkstoff" => 41, "Anz." => 59, "Länge" => 63, "Breite" => 69, "Höhe" => 74}

# method: loadDocument
# parameter: filePath
# returns: an array of hashmaps with elements
#
# This method loads the file and tests it's integrity based on the $indexHeaderMap and $rowHeaderMap
  def loadDocument(filePath)
    document = RubyXL::Parser.parse(filePath)

    worksheet = findWorksheet(document)

    if testFileIntegrity(worksheet)
      elements = identifyElements(worksheet, countElements(worksheet))
      #puts elements
      return elements
    else
      puts "ERROR: excelReader(Code: 0x03): File does not have fitting integrity"
    end


  end

# method: findWorksheet
# attributes: RubyXL::Worksheets document
# returns: RubyXL::Worksheet
#
# Tries to find the worksheet named  "Dimensionsware"
  def findWorksheet(document)
    for worksheet in document.worksheets
      if (worksheet.sheet_name == "Dimensionsware")
        return worksheet
      end
    end
    puts "ERROR: excelReader(Code: 0x01): no worksheet named Dimensionsware"
    return nil::NilClass
  end

# method: testFileIntegrity
# parameter: RubyXL::Worksheet worksheet
# returns: boolean
#
# Tests the files integrity by testing if the "Lfd" cell is at the supposed place
  def testFileIntegrity(worksheet)
    indexOfTableStart = findTableHeaderRow(worksheet)
    #puts "TableStartRowIndex = #{indexOfTableStart}"
    return indexOfTableStart == $rowOfHeader
  end

# method: identifyElements
# attributes: RubyXL::Worksheet, number
# returns: array of hashmaps defining an element
#
# Tries to parse from the given worksheet the given number of elements.
# This is done using the method: identifyElement
# NOTE: a valid file is necessary
  def identifyElements(worksheet, number)

    elements = []
    for count in 0..number - 1
      #printValuableInformation(worksheet[18 + 2 * count])
      #printRowFromTo(worksheet[18 + 2 * count], 13, 74)
      element = identifyElement(worksheet[18 + 2 * count])
      #puts element
      elements << element
    end

    return elements
  end

# method: countElements
# attributes: RubyXL::Worksheet
# returns: number of elements within this worksheet
#
# The method starts directly after the header row and counts how many elements are mentioned based on the "Lfd".
# If the number mentioned in this contains the substring "1000" then it is considered to be an element.
# After finding the first entry not considered to be an element it stops and returns the number of found elements
# NOTE: a valid file is necessary
# NOTE: maybe adjust criteria
  def countElements(worksheet)
    numberOfElements = 0
    count = 18
    worksheet.each_with_index {|row, index|
      isElement = false
      if index == count
        if row[$rowOfHeader].value.to_s.include? ("1000")
          #puts "Item with number #{row[$rowOfLfd].value.to_s} found"
          numberOfElements += 1
          isElement = true
        end

        if not isElement
          break
        end
        count += 2
      end
    }
    return numberOfElements
  end

# method: isExpectedFormat
# attributes: RubyXL::Row
# returns: boolean
#
# This method gets the header row as an input and checks, whether the header entries are at the expected
# columns stated in $indexHeaderMap. If this is not given the method return false otherwise true.
  def isExpectedFormat(row)
    correct = true
    for key in $indexHeaderMap.keys
      correct |= row[$indexHeaderMap[key]].value.to_s.include? key
    end
    return correct
  end

# method: findTableHeaderRow
# attributes: RubyXL::Worksheet
# returns: row Index
#
# This method traverses through the rows of the given worksheet and looks in the through $rowOfHeader
# defined column to search for the entry "Lfd".
  def findTableHeaderRow(worksheet)
    worksheet.each_with_index {|row, index|
      begin
        if row[$indexHeaderMap["Lfd"]].value.to_s.include? "Lfd."
          return index
        end
      rescue NoMethodError
      end
    }
    puts "ERROR: excelReader(Code: 0x02): no row with 'Lfd' at 16-th Cell"
    return -1
  end

# method: identifyElement
# attributes: RubyXL::Row
# returns: a hashmap
#
# This method gets a row and uses $indexValuesMap to parse information of an element expected to be in the row
# and parses this into a hashmap using the keys of $indexValuesMap.
# Note: does not filter empty elements (yet)
  def identifyElement(row)
    map = Hash.new
    for property in $indexValuesMap.keys.each {}
      map[property] = row[$indexValuesMap[property]].value.to_s
    end
    return map
  end

# PRINT METHODS

# method: printWorksheet
# attributes: worksheet, max: default = 50, printEmptyStrings: default = false
# returns : -none-
#
# This method takes a worksheet and prints the cells of the rows upto the max number given.
# Through printEmptyStrings one can decide, whether or not to print entries that do not contain a single symbol.
# Every row begins in a new line. This method uses the printRowWithIndexes-method
  def printWorksheet(worksheet, max = 50, printEmptyStrings = false)
    worksheet.each_with_index {|row, index|
      begin
        #if row.is_a? RubyXL::Row and row.size > 0
        if index >= $rowOfHeader and index <= max
          printRowWithIndexes(row, printEmptyStrings, index)
        end
      rescue NoMethodError
      end
    }
  end

# method: printRowWithIndexess
# attributes: RubyXL::Row, printEmptyStrings: default = false, rowIndex = -1
# returns: -none-
#
# This method prints a row of a worksheet into one line within the console.
# Through printEmptyStrings one can decide, whether or not to print entries that do not contain a single symbol.
# The rowIndex determines if not only the column of the cell should be printed as meta information, but also the index
# of the row within the worksheet. If it is stated as "-1" it is not printed, otherwise the given number is printed.
  def printRowWithIndexes(row, printEmptyStrings = false, rowIndex = -1)
    count = 0
    while count < row.size
      begin
        value_s = row[count].value.to_s
        if printEmptyStrings || value_s != ""
          if rowIndex > 0
            puts "[#{rowIndex}, #{count}]: #{value_s}"
          else
            puts "[#{count}]: #{value_s}"
          end

        end

        count += 1
      rescue NoMethodError
        if printEmptyStrings
          puts "[#{count}]: (nil)"
        end
      end
    end
  end

# method: printRowFromTo
# attributes: RubyXL::Row, from, to
# returns: -none-
#
# This method prints the cell-entries in the given row from the indexes <= from until the indexes <= to.
# if to is greater than the row size it is only considered until the last column
  def printRowFromTo(row, from, to)
    count = from
    while count <= [row.size, to].min
      begin
        value_s = row[count].value.to_s
        puts "[#{count}]: #{value_s}"

        count += 1
      rescue NoMethodError
        puts "[#{count}]: (nil)"
      end
    end
  end

end

#loadDocument("real_test1.xlsm")