# run this script with command `ruby copy_config_to_dubbo.rb`
#encoding: utf-8
require "yaml"
require File.dirname(__FILE__) + "/config_duplicate.rb"

class Copy_config

  def copy
    back_config = YAML.load_file("app/files/back_config.yaml")
    dubbo_config_file = File.open("app/files/back_config_dubbo.yaml", "w")
    copy_config = {"app"=>back_config["app"], "services"=>{}}

    back_config["services"].each do |key, value|
      service = value["uri"].scan(/^io.terminus.snz.(.*).service/).flatten[0]
      value["type"] = "DUBBO"
      app_hash = {"app"=> "snz-#{service}"}
      app_hash.merge!(value)
      value = app_hash
      copy_config["services"][key] = value
    end
    YAML::dump(copy_config, dubbo_config_file)
  end

end

if Check.new.check_back_config()
  Copy_config.new.copy()
end
