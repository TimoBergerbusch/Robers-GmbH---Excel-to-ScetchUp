require 'rubyXL'

$rowOfLfd=16

$indexHeaderMap = {"Lfd" => 13, "Sage Art." => 16, "Bezeichung Bauteil" => 17, "Materialgruppe / Werkstoff" => 37, "Anz." => 59, "Länge" => 63, "Breite" => 69, "Höhe" => 74}
$indexValuesMap = {"Lfd" => 13, "Sage Art." => 16, "Bezeichung" => 17, "Bauteil" => 21, "Materialgruppe"=>37,"Werkstoff" => 41, "Anz." => 59, "Länge" => 63, "Breite" => 69, "Höhe" => 74}


def findWorksheet(document)
  for worksheet in document.worksheets
    if (worksheet.sheet_name == "Dimensionsware")
      return worksheet
    end
  end
  puts "ERROR: excelReader(Code: 0x01): no worksheet named Dimensionsware"
  return nil::NilClass
end

def testFileIntegrity(worksheet)

  indexOfTableStart = findTableHeaderRow(worksheet)
  #puts "TableStartRowIndex = #{indexOfTableStart}"
  return indexOfTableStart
end

def identifyElements(worksheet, number)

  elements = []
  for count in 0..number-1
    #printValuableInformation(worksheet[18 + 2 * count])
    #printRowFromTo(worksheet[18 + 2 * count], 13, 74)
    element = identifyElement(worksheet[18+2*count])
    #puts element
    elements << element
  end

  return elements
end

def countElements(worksheet)
  numberOfElements = 0
  count = 18
  worksheet.each_with_index {|row, index|
    isElement = false
    if index == count
      if row[$rowOfLfd].value.to_s.include? ("1000")
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

def isAspectedFormat(row)
  correct = true
  for key in $indexHeaderMap.keys
    correct |= row[$indexHeaderMap[key]].value.to_s.include? key
  end
  return correct
end

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

def loadDocument(filePath)
  document = RubyXL::Parser.parse(filePath)

  worksheet = findWorksheet(document)

  if testFileIntegrity(worksheet) != -1
    elements = identifyElements(worksheet, countElements(worksheet))
    puts elements
  else
    puts "ERROR: excelReader(Code: 0x03): File does not have fitting integrity"
  end


end

def identifyElement(row)
  map = Hash.new
  for property in $indexValuesMap.keys.each{}
    map[property] = row[$indexValuesMap[property]].value.to_s
  end
  return map
end

# PRINT METHODS
def printWorksheet(worksheet, max=50,printEmptyStrings = false)
  worksheet.each_with_index {|row, index|
    begin
      #if row.is_a? RubyXL::Row and row.size > 0
      if index >= $rowOfLfd and index <= max
        printRowWithIndexes(row, printEmptyStrings, index)
      end
    rescue NoMethodError
    end
  }
end

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

loadDocument("real_test1.xlsm")