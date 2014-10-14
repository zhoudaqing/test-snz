#! /usr/bin/env ruby
#   encoding: utf-8

require "time"
require 'optparse'
require 'optparse/time'
require 'ostruct'

class ArgParseer
  attr_accessor :options
  @@options = nil

  def self.parse!(args)
    options = OpenStruct.new
    options.path = './'
    options.since = ''
    options.user = `whoami`
    options.enviroment = 'dev'
    options.project = []
    options.time = 'now'

    opt_parser = OptionParser.new do |opts|
      opts.banner = "Generate relase log. @ohhYumYum\n\n" + \
                    "Usage: rlog.rb [options] -p PROJECTS... -s SINCE"

      opts.separator ""
      opts.separator "Specific options:"

      opts.on("-d", "--dir PATH_TO_PROJECT",
              "Path to project.") do |p|
        options.path = p || './'
      end

      # enviroment the system deploy to.
      opts.on("-e", "--env [ENV]",
              "Deploy system to which enviroment.") do |envs|
        options.enviroment = envs || 'dev'
      end

      # which projects been deployed
      opts.on("-p", "--project PROJECT,PROJECT", Array,
              "Required! Specify which project upload and released.") do |project|
        options.project = project
      end

      # Last commit of preview's
      opts.on("-s", "--since COMMIT",
              "Required! Last commit of previews deploying.") do |since|
        options.since = since
      end

      # time when release the system
      opts.on("-t", "--time [TIME]",
              "The time when relase the system.") do |time|
        options.time = time || 'now'
      end

      # user who release the system
      opts.on("-u", "--user [USRE]",
              "The user who release the system.") do |user|
        options.user = user || `whoami`.chomp
      end

      opts.separator ""
      opts.separator "Common options:"

      # No argument, shows at tail.  This will print an options summary.
      # Try it and see!
      opts.on_tail("-h", "--help", "Show this message") do
        puts opts
        exit
      end

      # Another typical switch to print the version.
      opts.on_tail("--version", "Show version") do
        puts ::Version.join('.')
        exit
      end
    end

    opt_parser.parse!(args)

    valid options

    options
  end

  def self.valid options
    missing "Project" if options.project.nil? or options.project.empty?
    missing "Since what commit" if options.since.empty? or options.since.nil?
  end

  def self.missing(mod)
    puts mod + " is required."
    exit
  end
end

options = ArgParseer.parse! ARGV

pretty_log = ''
path = options.path
since = options.since
gitlab_user = options.user
enviroment = options.enviroment
project = options.project
time = options.time
home = `cd ~ && pwd`.chomp
log_file =  File.join(home, 'release-log.md')

format = %q(format:'%h%C(reset) %s%n(%ar by %an)%d%C(reset)%n')
def get_env(s)
  if s == 'dev'
    '测试环境'
  elsif s == 'prod'
    '生产环境'
  end
end

Dir.chdir path do
  branch = `git rev-parse --abbrev-ref HEAD`
  if time == 'now'
    time = Time.now.strftime '%Y-%m-%d %T'
  else
    time = Time.now.strftime('%Y-%m-%d ') + time
  end

  # 2014-01-01 11:11:11 测试环境\n
  pretty_log << "### #{time} #{get_env enviroment}\n\n"
  pretty_log << "#{project.join ','} #{branch}\n"

  log = `git log --pretty=#{format} #{since}...HEAD`

  log.each_line do |line|
    if line.chomp.empty?
      pretty_log << ">\n"
    elsif line =~ /\w.*/
      pretty_log << "> #{line.chomp}  \n"
    else
      pretty_log << "> #{line}"
    end
  end
end

pretty_log.gsub! '[m', ''
pretty_log << "\nby @#{gitlab_user}\n"

File.open log_file, 'w+' do |file|
  file << pretty_log
end

puts "release log have bean create under #{log_file} and copied to your clip board."
exec "cat #{log_file}|pbcopy"
