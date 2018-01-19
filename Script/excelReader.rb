require 'rubyXL'

module ExcelReader

  #excel = WIN32OLE.new('Excel.Application')
  #excel.Visible = false
  #wb = excel.Workbooks.Open('C:\\Users\\Timo Bergerbusch\\AppData\\Roaming\\SketchUp\\SketchUp 2018\\SketchUp\\Plugins\\su_automaticExcelConvert\\Mappe1.xlsm')
  #wb = wb.Worksheets(1)

  #puts wb

  workbook = RubyXL::Parser.parse("real_test1.xlsm")

  for worksheet in workbook.worksheets
    if(worksheet.sheet_name == "Dimensionsware")
      worksheet.each{|x|
        count = 0
        used = false
        while count < x.size
          if x[count].is_a? RubyXL::Cell
            if not (x[count].value.to_s == "" )
              print "#{x[count].value} "
              used = true
            end
          end
          count+=1
        end
        if used
          puts
        end
      }
    end
  end

end
