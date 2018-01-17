require_relative 'classes/rectangle'
def isLoaded
  puts "Successfully loaded."
end

$face= 0

def testRectangle
  model = Sketchup.active_model
  #entities = model.active_entities
  material1 = model.materials.add("Test Material Wood")
  material1.texture = "C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture.jpg"
  material2 = model.materials.add("Test Material Iron")
  material2.texture = "C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture2.jpg"

  #puts File.exist?("C:/Users/Timo Bergerbusch/AppData/Roaming/SketchUp/SketchUp 2018/SketchUp/Plugins/classes/texture.jpg")

  #face.material = material
  #face.position_material(material, face.vertices, true)

  d = Rectangle.new(50.mm , 400.mm, 1000.mm, Geom::Vector3d.new(10,10,10), [material1, material2])
  faces = d.draw(model)
  return faces
end

SKETCHUP_CONSOLE.show

UI.menu("Plugins").add_item("Create Rectangle") {
  face = testRectangle
  $face = face
}

UI.menu("Plugins").add_item("Create and Move Rectangle") {

  Sketchup.active_model.active_entities.each{|x|
    if x.is_a? Sketchup::Face
      puts x.material.name
    end
  }
  puts Sketchup.active_model.active_entities.length
  #puts $face.material
  #rec = testRectangle
  #Sketchup.active_model.entities.transform_by_vectors([rec], [[0,0,10]])
  #Sketchup.active_model.entities.transform_entities(Geom::Transformation.new([10,0,0]), rec)
  #rec.transform!([10,0,0])
}

