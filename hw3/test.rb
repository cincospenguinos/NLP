#!/usr/bin/env ruby
# test.rb
# I'm sick of using test, so I'm building my own test script using the greatest
# scripting language in the world, Ruby!
require 'thor'

class Test < Thor

  desc 'Run test', 'Run test against the trace files'
  def test(feature='WORD', type='train')
    run_test(feature, type)
  end

  private

  def run_test(feature, type)
    `ant clean &> /dev/null`

    if system('ant >> build.log')
      File.delete('build.log')

      features = 'WORD'

      if feature == 'ALL'
        features = 'WORD WORDCON POS POSCON ABBR CAP LOCATION'
      elsif feature != 'WORD'
        features += " #{feature}" if feature != 'WORD'
      end

      `java -jar target/nlp_hw3.jar ner-input-files/train.txt ner-input-files/test.txt ner-input-files/locs.txt #{features}`
      `echo 'diff #{type}.txt.readable ner-trace-files/#{type}.txt.readable.#{feature} > diff.txt' > diff.txt`
      `diff #{type}.txt.readable ner-trace-files/#{type}.txt.readable.#{feature} >> diff.txt`
      if `cat diff.txt | wc -l`.to_i <= 10
        puts `cat diff.txt`
      else
        puts 'There were probably some errors'
      end
    else
      puts 'Build failed.'
    end
  end
end

Test.start(ARGV)