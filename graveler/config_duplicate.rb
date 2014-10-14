# run this script with command `ruby config_duplicate.rb`

require "yaml"

class Check

  def check_back_config
    back_config_file = File.open("app/files/back_config.yaml")
    url_list = Array.new()
    status = true

    back_config_file.each do |line|
      if line != "\n" and line.match(/uri: \".*\"$/)
        uri = line.scan(/uri: "(.*)"/).flatten[0]
        if url_list.include?(uri)
          status = false
          puts "back_config services repeat:\n lineNo: #{back_config_file.lineno} uri: #{uri}"
        else
          url_list << "#{uri}"
        end
      end
    end

    return status
  end

  def check_front_config_mapping
    front_config = YAML.load_file("app/files/front_config.yaml")
    mapping_list = Array.new()

    front_config["mappings"].each do |i, n|
      if mapping_list.include?("{#{i["pattern"]}, #{i["methods"]}}")
        puts "front_config mapping repeat:\n  #{i}"
      else
        mapping_list << "{#{i["pattern"]}, #{i["methods"]}}"
      end
    end
  end

  def check_front_config_component
    front_config_file = File.open("app/files/front_config.yaml")
    component_list = Array.new()

    front_config_file.each do |line|
      if line != "\n" and line.match(/\".*\":$/)
        path = line.scan(/"(.*)":$/).flatten[0]
        component_list.include?(path) ? "#{puts "front_config component-path repeat:\n  lineNo: #{front_config_file.lineno} path: #{path}"}" : "#{component_list << path}"
      end
    end
  end

end

Check.new.check_front_config_mapping()
Check.new.check_front_config_component()
Check.new.check_back_config()
