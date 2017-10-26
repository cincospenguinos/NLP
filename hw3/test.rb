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

  desc 'Check against accuracy', 'Run the program and check against accuracy'
  def accuracy(feature='WORD', type='train')
    run_test(feature, type, false)

    Dir.chdir('liblinear-1.93') do
      `make clean &> /dev/null`
      
      if system('make &> /dev/null')
        `mv predict ..`
        `mv train ..`
      else
        puts 'Could not make liblinear!'
        return 1
      end
    end

    `./train train.txt.vector models.txt`
    `./predict test.txt.vector models.txt predictions.txt.#{feature} > accuracy.txt.#{feature}`
    `echo 'diff predictions.txt.#{feature} ner-trace-files/predictions.txt.#{feature}' > diff.txt`
    `diff predictions.txt.#{feature} ner-trace-files/predictions.txt.#{feature} >> diff.txt`
    `echo 'diff accuracy.txt.#{feature} ner-trace-files/accuracy.txt.#{feature}' >> diff.txt`
    `diff accuracy.txt.#{feature} ner-trace-files/accuracy.txt.#{feature} >> diff.txt`
    `cat diff.txt`
  end

  private

  def run_test(feature, type, output=true)
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
        puts `cat diff.txt` if output
      else
        puts 'There were probably some errors' if output
      end
    else
      puts 'Build failed.'
    end
  end
end

Test.start(ARGV)