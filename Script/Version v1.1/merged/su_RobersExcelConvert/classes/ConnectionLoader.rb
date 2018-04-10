require 'FileUtils'
require_relative 'rectangle'
require_relative 'materialHandler'

class ConnectionLoader


  def initialize(file)
    @defaultGeneralElements = ["numberOfElements", "usedMaterials"]
    @defaultElementElements = ["name", "vorne", "hinten", "links", "rechts", "oben", "unten", "x-achse", "y-achse", "z-achse", "offX", "offY", "offZ"]
    @file = file
    @ini = nil
    @materials = Hash.new
    preloadMaterials

    @data = Hash.new
    @data["Elements"] = Hash.new
    @data["Rectangles"] = Hash.new
    reloadFile
  end

  define_method(:reloadFile) do
    filePath = File.dirname(@file)
    if not File.exist? filePath then
      puts "WARNING(0x01): ConnectionLoader: No connection.ini found"
    else
      @ini = IniFile.load(@file)
    end
  end

  define_method(:isLoaded?) do
    return @ini != nil
  end

  define_method (:readElements) do
    if isLoaded? then
      generals = readGeneral
      @data["Generals"] = generals
      for i in 1..generals["numberOfElements"]
        # puts "Element number #{i}"
        element = readElement(i)
        @data["Elements"]["Element#{i}"] = element
        rectangle = createRectangleFromElement(element)
        @data["Rectangles"]["Rectangle#{i}"] = rectangle
      end
    end
  end

  def readElement(number)
    element = Hash.new
    section = "Element#{number}"
    @defaultElementElements.each do |key|
      element[key] = @ini[section][key]
    end

    return element
  end

  def printData()
    puts "_____________Data_____________"

    @data.each_key do |key|
      value = @data[key]
      if value.is_a?(Hash) then
        puts key
        value.each_key {|innerKey|
          puts "    #{value[innerKey]}"
        }
      else
        puts "#{key}:#{@data["key"]}              #{value.class}"
      end
    end
    puts "______________________________"
  end

  def preloadMaterials
    files = Dir[File.expand_path("..", __dir__) + "/textures/**"]
    #puts File.basename files[0]

    files.each do |file|
      filename = File.basename(file).split(".")[0]
      # puts filename
      material = Sketchup.active_model.materials.add(filename)
      material.texture = file
      @materials[filename] = material
    end
  end

  def createRectangleFromElement(element)
    width = element["x-achse"].mm
    depth = element["y-achse"].mm
    height = element["z-achse"].mm
    offset = Geom::Vector3d.new(element["offX"].mm, element["offY"].mm, element["offZ"].mm)
    materialHandler = createMaterialHandlerFromElement(element)
    name = element["name"]
    return Rectangle.new(height, width, depth, offset, materialHandler, name)
  end

  def createMaterialHandlerFromElement(element)
    vorne = getMaterial(element["vorne"])
    hinten = getMaterial(element["hinten"])
    links = getMaterial(element["links"])
    rechts = getMaterial(element["rechts"])
    oben = getMaterial(element["oben"])
    unten = getMaterial(element["unten"])
    return MaterialHandler.new([vorne, hinten, links, rechts, oben, unten])
  end

  def getMaterial(name)
    material = @materials[name]
    if material.is_a?(NilClass) then
      material = @materials["errorTexture"]
    end
    return material
  end

  def getRectangles()
    return @data["Rectangles"]
  end

  define_method(:readGeneral) do
    generalMap = Hash.new

    @defaultGeneralElements.each do |key|
      if key == "usedMaterials" then
        generalMap[key] = getMaterialsAsArray(@ini["General"][key])
      else
        generalMap[key] = @ini["General"][key]
      end
    end

    return generalMap
  end

  define_method (:getMaterialsAsArray) do |string|
    return string.split("|")
  end

  def readFile
    if File.file?(@file)
      readGeneral
      readElements
      clearFile
      return getRectangles
    else
      puts "No connection.ini to read available"
      UI.messagebox("No connection.ini to read available", type = MB_OK)
      return Hash.new
    end

  end

  def clearFile
    #FileUtils.remove(@file)
  end

end