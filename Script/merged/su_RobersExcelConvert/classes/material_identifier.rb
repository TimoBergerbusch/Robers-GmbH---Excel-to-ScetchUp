require 'inifile'
require_relative 'materialHandler'

class MaterialIdentifier


  def initialize
    @materials = Hash.new
    @materialAssignment = Hash.new
    filePath = File.dirname(__FILE__) + "/materials.ini"
    if not File.exist? filePath then
      puts "ERROR: MaterialIdentifier(Code: 0x01): NO material.ini existing"
    end
    @file = IniFile.load(filePath)

    initializeMaterials
    initializeMaterialAssignments
  end

  def initializeMaterials
    puts "Test Start"

    files = Dir[File.expand_path("..", __dir__) + "/textures/**"]
    #puts File.basename files[0]

    files.each do |file|
      filename = File.basename(file).split(".")[0]
      puts filename
      material = Sketchup.active_model.materials.add(filename)
      material.texture = file
      @materials[filename] = material
    end

    puts "Test Ende"
  end

  def initializeMaterialAssignments()
    @file.each_section do |section|
      puts section
      vorne = @materials[@file[section]["vorne"]]
      hinten = @materials[@file[section]["hinten"]]
      links = @materials[@file[section]["links"]]
      rechts = @materials[@file[section]["rechts"]]
      oben = @materials[@file[section]["oben"]]
      unten = @materials[@file[section]["unten"]]

      materialHandler = MaterialHandler.new([vorne, hinten, links, rechts, oben, unten])
      assignment = Hash.new
      assignment["key"] = @file[section]["key"]
      assignment["name"] = @file[section]["name"]
      assignment["materialgruppe"] = @file[section]["materialgruppe"]
      assignment["werkstoff"] = @file[section]["werkstoff"]
      assignment["MaterialHandler"] = materialHandler

      @materialAssignment[@file[section]["key"]] = assignment
    end
  end

  def identifyMaterialHandler(materialgruppe, werkstoff)

    @materialAssignment.each_key do |key|
      if @materialAssignment[key]["materialgruppe"] == materialgruppe
        if not werkstoff.is_a?(NilClass) or werkstoff.to_s.include?(@materialAssignment[key]["werkstoff"])
          puts "Es passt zu #{key}"
          return @materialAssignment[key]["MaterialHandler"]
        end
      end
    end
    puts "Nichts passt zu #{materialgruppe} und #{werkstoff}"
  end

  def printAssignments
    puts @materialAssignment
  end

  def getMaterial(key)
    return @materials[key]
  end

  def test
    puts @materials
  end

end