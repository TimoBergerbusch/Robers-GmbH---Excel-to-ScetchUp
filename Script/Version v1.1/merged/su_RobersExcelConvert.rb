require 'inifile'
require_relative 'su_RobersExcelConvert/classes/materialHandler'
require_relative 'su_RobersExcelConvert/classes/entityHandler'
require_relative 'su_RobersExcelConvert/classes/rectangle'
require_relative 'su_RobersExcelConvert/classes/ConnectionLoader'

#require_relative 'su_RobersExcelConvert/main'

$path = File.dirname(__FILE__) + '/su_RobersExcelConvert'
$texturePath = $path + '/textures'
$EntityHandler = EntityHandler.new


# method: isLoadede
# parameters: -none-
# returns: -none-
#
# A simple test method in the Ruby-Console of SketchUp to test whether the Plugin is loaded
def isLoaded
  puts "Successfully loaded."
end

def loadToolbar
  toolbar = UI::Toolbar.new("Robers Excel Convert")

  readExcelCommand = UI::Command.new("Read Connection.ini") {
    file = $path + "/classes/connection.ini"
    if not file.nil? then
      # UI.messagebox("Start reading the excel. This may take a few seconds.", type = MB_OK)
      readConnectionFile file
    end


  }
  readExcelIcon = Sketchup.find_support_file("paintbrush.png", "Plugins/su_RobersExcelConvert/Icons/")
  readExcelCommand.large_icon = readExcelIcon
  readExcelCommand.small_icon = readExcelIcon
  readExcelCommand.tooltip = "Open a xlsm-file"
  toolbar.add_item readExcelCommand

  UI.start_timer(0.1, false) {
    toolbar.restore
  }


  UI.menu("Plugins").add_item("Max Plugin") {

    prompts = ["Nächstes Maß:"]
    defaults = [""]
    #input = UI.inputbox(prompts, defaults, "Tell me about yourself.")
    xAxis = UI.inputbox(prompts, defaults, "Schnelle Eingabe")
    yAxis = UI.inputbox(prompts, defaults, "Schnelle Eingabe")
    zAxis = UI.inputbox(prompts, defaults, "Schnelle Eingabe")

    # UI.messagebox("x-achse: #{xAxis[0].to_i.mm} \n y-achse: #{yAxis[0].to_i.mm} \n z-achse: #{zAxis[0].to_i.mm} ", type = MB_OK)

    rec = Rectangle.new(xAxis[0].to_i.mm, yAxis[0].to_i.mm, zAxis[0].to_i.mm, Geom::Vector3d.new(10.mm, 10.mm, 10.mm), MaterialHandler.new([]), "Generated")
    rec.draw(Sketchup.active_model)

  }
end

def readConnectionFile(file)
  cl = ConnectionLoader.new(file)
  # cl.readGeneral
  # cl.readElements
  #rectangles = cl.getRectangles
  rectangles = cl.readFile

  $EntityHandler.deleteAll
  rectangles.each_key {|key|
    $EntityHandler.addRectangle(rectangles[key])
  }
  #$EntityHandler.drawAll
end

# execute the load
loadToolbar
